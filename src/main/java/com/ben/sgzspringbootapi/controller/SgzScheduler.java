package com.ben.sgzspringbootapi.controller;

import com.ben.sgzspringbootapi.entity.*;
import com.ben.sgzspringbootapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Calendar.HOUR_OF_DAY;

//会员分销系统日结程序
@RestController
@RequestMapping("/calculate")
public class SgzScheduler {

    private Logger logger = LoggerFactory.getLogger(SgzScheduler.class);

    //确保时区正确
    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private BonusService bonusService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private SgzUtil sgzUtil;

    private Map levelMap = new HashMap();

    //设定每晚11：50分运行会员计费日结程序
    @Scheduled(cron = "0 50 23 * * *")
    @RequestMapping("/nightBatch")
    public Result nightBatch(){
        List<Level> levelList = levelService.LevelList();
        for(int i = 0 ; i < levelList.size() ; i++){
            levelMap.put(levelList.get(i).getLevelID(), levelList.get(i));
        }
        logger.info("Put the level information from table to Map in Memory ");
        Result temp;
        temp = userLevelUpgrade();
        System.out.println(temp);
        temp = summaryUserOrder();
        System.out.println(temp);
        temp = calculateNominate();
        System.out.println(temp);
        temp = calDPbonus();
        System.out.println(temp);
        temp = calDPlevel1();
        System.out.println(temp);
        temp = calDPlevel2();
        System.out.println(temp);
        temp = calDPlevel3();
        System.out.println(temp);
        temp = calculateAccount();
        System.out.println(temp);
        temp = updateteamsize();
        System.out.println(temp);
        temp = summaryUpdate();
        System.out.println(temp);
        temp = summaryUpdateTodayTeamBonus();
        System.out.println(temp);
        temp = updateGradeAndFund();
        System.out.println(temp);
        //日结之后将所有用户表的团队当日业绩重置0， update user set lastteamamt = 0
        userService.resetLastteamamt();
        //用户只需要公司业绩汇总，只留下子节点汇总信息。
        summaryService.onlyKeepCompanySummary();
        return temp;
    }

    //会员自动升级逻辑, 根据累计业绩自动升级
    @RequestMapping("/userLevelUpgrade")
    public Result userLevelUpgrade(){
        double levelAmt[] = new double[5];
        //put level amount to array
        for (int i=0; i<5; i++){
            Level tempLevel = (Level) levelMap.get(i+1);
            levelAmt[i] =  tempLevel.getTotalamt();
        }
        //更新会员级别，每个会员只能最多升级两次
        List<User> userList = userService.userList();
        for(int i=0; i<userList.size(); i++){
            User user = userList.get(i);
            //每个会员只能最多升级两次，包括第一次注册时初始化的升级
            if (user.getUpgrade()<2){
                Account account = accountService.selectAccountByUserID(user.getUserId());
                if (account.getTotalOrderAmt() < levelAmt[0]){
                    continue;
                } else if (account.getTotalOrderAmt() > levelAmt[4]){
                    //升级为最高级时，不需要再升级
                    user.setLevel(5);
                    user.setUpgrade(2);
                    logger.info("Night Batch: Updating the user " + user.getUserId() + " to " + user.getLevel());
                    userService.updateUser(user);
                } else {
                    for (int j=0; j<4; j++){
                        if ((account.getTotalOrderAmt()>=levelAmt[j])&&(account.getTotalOrderAmt()<levelAmt[j+1])){
                            user.setLevel(j+1);
                            int upgrade = user.getUpgrade()+1;
                            user.setUpgrade(upgrade);
                            logger.info("Night Batch: Updating the user " + user.getUserId() + " to " + user.getLevel());
                            userService.updateUser(user);
                            break;
                        }
                    }
                }
            } else {
                continue;
            }
        }
        return ResultService.success();

    }

    /*
    1.获取用户列表，将当日团队订单总业绩从user表搬到summary表summary.setTeamttorder(user.getLastteamamt())；
    2.然后写入一条新的summary记录，其它个人业绩ttorder和奖励ttbonus和团队奖励Teamttbonus为空；
    3.根据当日团队业绩和对应级差grade的会员根据levelMap里面的比例，计算出主任，经理，董事等对应的级差奖励。
     */
    @RequestMapping("/summaryUserOrder")
    public Result summaryUserOrder(){
        double todayTotal = 0;
        List<User> userList = userService.userList();
        Date date = new Date();
        for( int i = 0 ; i < userList.size() ; i++) {
            User user = userList.get(i);
            Summary summary = new Summary();
            summary.setUserID(user.getUserId());
            summary.setSummaryDate(date);
            summary.setTeamttorder(user.getLastteamamt());
            //Remember the lastteamamout of root member, as this value is for total company and use to calculate the fund
            if (user.getTreePosition()==1){
                todayTotal = user.getLastteamamt();
            }
            summaryService.addNewSummary(summary);
            //calculate the grade bonus during summary;
            if (user.getGrade()!=0){
                Level templevel = (Level) levelMap.get(user.getGrade());
                if ((templevel!=null)&&(user.getLastteamamt()!=0)) {
                    Bonus bonus = new Bonus();
                    bonus.setUserID(user.getUserId());
                    bonus.setRelatedUserID(user.getUserId());
                    bonus.setBonusType("grade");
                    bonus.setBonusAmt(user.getLastteamamt()*templevel.getTotalamt()/100);
                    bonus.setBonusdetail("级差奖励 :"+ user.getUserId() +" 基数:"+ user.getLastteamamt()+" × 比例:"+ templevel.getTotalamt()+"%");
                    bonus.setBonusDate(CurrentDate());
                    logger.info("Add new bonus information for: "+ bonus.getUserID() + " bonus detail: " + bonus.getBonusdetail());
                    bonusService.addNewBonus(bonus);
                }
            }

        }
        //calculate the Grade & fund Bonus
        for (int i=0 ; i < userList.size(); i++){
            User user = userList.get(i);
            if (user.getFund()==0){
                continue;
            } else {
                Level templevel = (Level) levelMap.get(user.getFund());
                if (templevel!=null) {
                    double gradefactor = templevel.getTotalamt();
                    double fundfactor = templevel.getNominate();
                    if ((gradefactor!=0) && (todayTotal!=0)){
                        Bonus bonus = new Bonus();
                        bonus.setUserID(user.getUserId());
                        bonus.setRelatedUserID(user.getUserId());
                        bonus.setBonusType("grade");
                        bonus.setBonusAmt(todayTotal*gradefactor/100);
                        bonus.setBonusdetail("级差奖励 :"+ user.getUserId() +" 基数:"+ todayTotal +" × 比例:"+ gradefactor +"%");
                        bonus.setBonusDate(CurrentDate());
                        logger.info("Add new bonus information for: "+ bonus.getUserID() + " bonus detail: " + bonus.getBonusdetail());
                        bonusService.addNewBonus(bonus);
                    }
                    if ((fundfactor!=0) && (todayTotal!=0)){
                        Bonus bonus = new Bonus();
                        bonus.setUserID(user.getUserId());
                        bonus.setRelatedUserID(user.getUserId());
                        bonus.setBonusType("fund");
                        bonus.setBonusAmt(todayTotal*fundfactor/100);
                        bonus.setBonusdetail("福利基金 :"+ user.getUserId() +" 基数:"+ todayTotal +" × 比例:"+ fundfactor +"%");
                        bonus.setBonusDate(CurrentDate());
                        logger.info("Add new bonus information for: "+ bonus.getUserID() + " bonus detail: " + bonus.getBonusdetail());
                        bonusService.addNewBonus(bonus);
                    }
                }
            }
        }

        return ResultService.success();
    }

    /*
    1.获取当日所有订单的列表；
    2.根据用户表找到推荐人；
    3.根据每张订单的金额计算出推荐奖金额。
     */
    @RequestMapping("/calNominate")
    public Result calculateNominate(){
        List<UserOrder> userOrderList = userOrderService.orderListByDate(CurrentDate());
        for( int i = 0 ; i < userOrderList.size() ; i++) {
            UserOrder order = userOrderList.get(i);
            if ((userService.selectbyUserId(order.getUserId()).getNominator()==0)) continue;
            Bonus bonus = new Bonus();
            bonus.setUserID(userService.selectbyUserId(order.getUserId()).getNominator());
            bonus.setRelatedUserID(order.getUserId());
            bonus.setBonusType("nominate");
            bonus.setBonusAmt(order.getAmount()*0.1);
            bonus.setBonusdetail("快乐奖 会员："+ order.getUserId() +" 基数:"+order.getAmount()+" × 比例:"+ 10 +"%");
            bonus.setBonusDate(CurrentDate());
            logger.info("Add new bonus information for: "+ bonus.getUserID() + " bonus detail: " + bonus.getBonusdetail());
            bonusService.addNewBonus(bonus);
        }
        return ResultService.success();
    }

    /*
    1.获取有两个小市场的会员列表；
    2.根据对碰基数计算对碰奖；
    3.对碰基数同时减去，明天订单继续累计。
     */
    @RequestMapping("/calDPbonus")
    public Result calDPbonus(){
        List<User> userList = userService.userListBothLeg();
        for( int i = 0 ; i < userList.size() ; i++) {
            User user = userList.get(i);
            //Get the DP Facter from levelmap
            Level templevel = (Level) levelMap.get(user.getLevel());
            double Duipeng = templevel.getDuipeng();
            double DPlevel1 = templevel.getDplevel1();
            //Get the DP Facter from levelmap
            User leftUser = userService.selectbyUserId(user.getTreeLeft());
            User rightUser = userService.selectbyUserId(user.getTreeRight());
            Bonus bonus = new Bonus();
            if ((leftUser.getCalamount()==0)||(rightUser.getCalamount()==0)){
                continue;
            } else {
                if (leftUser.getCalamount()<=rightUser.getCalamount()) {
                    bonus.setRelatedUserID(leftUser.getUserId());
                    double tempBonusAmt = (leftUser.getCalamount() * Duipeng)/100;
                    bonus.setBonusAmt(tempBonusAmt);
                    bonus.setBonusdetail("幸运奖 会员："+ leftUser.getUserId() +" 基数:"+leftUser.getCalamount()+" × 比例:"+ Duipeng +"%");
                    rightUser.setCalamount(rightUser.getCalamount()-leftUser.getCalamount());
                    leftUser.setCalamount(0);
                } else{
                    bonus.setRelatedUserID(rightUser.getUserId());
                    double tempBonusAmt = (rightUser.getCalamount() * Duipeng)/100;
                    bonus.setBonusAmt(tempBonusAmt);
                    bonus.setBonusdetail("幸运奖 会员："+ rightUser.getUserId() +" 基数:"+rightUser.getCalamount()+" × 比例:"+ Duipeng+"%");
                    leftUser.setCalamount(leftUser.getCalamount()-rightUser.getCalamount());
                    rightUser.setCalamount(0);
                }
                bonus.setUserID(user.getUserId());
                bonus.setBonusType("DPbonus");
                System.out.println(bonus.getBonusDate());
                bonus.setBonusDate(CurrentDate());
                System.out.println(bonus.getBonusDate());
                logger.info("Add new bonus information for: "+ bonus.getUserID() + " bonus detail: " + bonus.getBonusdetail());
                bonusService.addNewBonus(bonus);
                userService.updateUser(leftUser);
                userService.updateUser(rightUser);
            }

        }
        return ResultService.success();
    }

    /*
    1.获取当日已经计算出来的对碰奖列表；
    2.根据已经有的对碰奖算出第一代互助奖
     */
    @RequestMapping("/calDPlevel1")
    public Result calDPlevel1(){
        Date date = new Date();
        List<Bonus> bonusList = bonusService.bonusListByDate(date);
        for( int i = 0 ; i < bonusList.size() ; i++) {
            Bonus bonus = bonusList.get(i);
            if (bonus.getBonusType().equalsIgnoreCase("DPbonus")){
                User currentUser = userService.selectbyUserId(bonus.getUserID());
                if (currentUser.getParent()==0){
                    continue;
                } else{
                    User parentUser = userService.selectbyUserId(currentUser.getParent());
                    //Get the DP Facter from levelmap
                    Level templevel = (Level) levelMap.get(parentUser.getLevel());
                    double DPlevel1 = templevel.getDplevel1();
                    double DUIPENG = templevel.getDuipeng();
                    //Get the DP Facter from levelmap
                    if (DPlevel1!=0) {
                        Bonus level2bonus = new Bonus();
                        level2bonus.setUserID(parentUser.getUserId());
                        level2bonus.setRelatedUserID(currentUser.getUserId());
                        level2bonus.setBonusType("dplevel1");
                        level2bonus.setBonusAmt(bonus.getBonusAmt()*DUIPENG*10/DPlevel1);
                        level2bonus.setBonusdetail("第1代互助奖基于 会员："+ currentUser.getUserId() +" 幸运奖基数:"+bonus.getBonusAmt()*100/DPlevel1 +" × 比例:"+ DPlevel1+"%");
                        level2bonus.setBonusDate(date);
                        logger.info("Add new bonus information for: "+ bonus.getUserID() + " bonus detail: " + bonus.getBonusdetail());
                        bonusService.addNewBonus(level2bonus);
                    }
                }
            } else {
                continue;
            }

        }
        return ResultService.success();
    }

    /*
    1.获取当日已经计算出来的对碰奖列表；
    2.根据已经有的第1代互助奖算出第2代互助奖
     */
    @RequestMapping("/calDPlevel2")
    public Result calDPlevel2(){
        Date date = new Date();
        List<Bonus> bonusList = bonusService.bonusListByDate(date);
        for( int i = 0 ; i < bonusList.size() ; i++) {
            Bonus bonus = bonusList.get(i);
            if (bonus.getBonusType().equalsIgnoreCase("dplevel1")){
                User currentUser = userService.selectbyUserId(bonus.getUserID());
                if (currentUser.getParent()==0){
                    continue;
                } else{
                    User parentUser = userService.selectbyUserId(currentUser.getParent());
                    //Get the DP Facter from levelmap
                    Level templevel = (Level) levelMap.get(parentUser.getLevel());
                    double DPlevel1 = templevel.getDplevel1();
                    double DPlevel2 = templevel.getDplevel2();
                    //Get the DP Facter from levelmap
                    if (DPlevel2!=0) {
                        Bonus level2bonus = new Bonus();
                        level2bonus.setUserID(parentUser.getUserId());
                        level2bonus.setRelatedUserID(currentUser.getUserId());
                        level2bonus.setBonusType("dplevel2");
                        level2bonus.setBonusAmt(bonus.getBonusAmt()*DPlevel2/DPlevel1);
                        level2bonus.setBonusdetail("第2代互助奖：基于第1代互助奖 会员："+ currentUser.getUserId() +"的奖金"+bonus.getBonusAmt()+"产生。 第1代比例："+ DPlevel1+"%, 第2代比例:"+DPlevel2);
                        level2bonus.setBonusDate(date);
                        logger.info("Add new bonus information for: "+ bonus.getUserID() + " bonus detail: " + bonus.getBonusdetail());
                        bonusService.addNewBonus(level2bonus);
                    }
                }
            } else {
                continue;
            }

        }
        return ResultService.success();
    }

    /*
    1.获取当日已经计算出来的对碰奖列表；
    2.根据已经有的第2代互助奖算出第3代互助奖
     */
    @RequestMapping("/calDPlevel3")
    public Result calDPlevel3(){
        Date date = new Date();
        List<Bonus> bonusList = bonusService.bonusListByDate(date);
        for( int i = 0 ; i < bonusList.size() ; i++) {

            Bonus bonus = bonusList.get(i);
            if (bonus.getBonusType().equalsIgnoreCase("dplevel2")){
                User currentUser = userService.selectbyUserId(bonus.getUserID());
                if (currentUser.getParent()==0){
                    continue;
                } else{
                    User parentUser = userService.selectbyUserId(currentUser.getParent());
                    //Get the DP Facter from levelmap
                    Level templevel = (Level) levelMap.get(parentUser.getLevel());
                    double DPlevel2 = templevel.getDplevel2();
                    double DPlevel3 = templevel.getDplevel3();
                    //Get the DP Facter from levelmap
                    if (DPlevel3!=0) {
                        Bonus level2bonus = new Bonus();
                        level2bonus.setUserID(parentUser.getUserId());
                        level2bonus.setRelatedUserID(currentUser.getUserId());
                        level2bonus.setBonusType("dplevel3");
                        level2bonus.setBonusAmt(bonus.getBonusAmt()*DPlevel3/DPlevel2);
                        level2bonus.setBonusdetail("第3代互助奖：基于第2代互助奖 会员："+ currentUser.getUserId() +"的奖金"+bonus.getBonusAmt()+"产生。 第2代比例："+ DPlevel2+"%, 第3代比例:"+DPlevel3);
                        level2bonus.setBonusDate(date);
                        logger.info("Add new bonus information for: "+ bonus.getUserID() + " bonus detail: " + bonus.getBonusdetail());
                        bonusService.addNewBonus(level2bonus);
                    }
                }
            } else {
                continue;
            }
        }
        return ResultService.success();
    }

    @RequestMapping("/bonusListByDate")
    public Result bonusListByDate(){
        Date date = new Date();
        return ResultService.success(bonusService.bonusListByDate(date));
    }

    /*
    1.获取当日所有奖金列表，包括级差，推荐奖，对碰奖；
    2.根据奖金列表加入到账户列表，更新TotalBonus, AvailBonus奖金总额，可提现奖金，withdrawPercent, TotalOrderAmt留空。
    3.根据用户的总业绩，更新用户的级别level。
     */
    @RequestMapping("/account")
    public Result calculateAccount(){
        Date date = new Date();
        List<Bonus> bonusList = bonusService.bonusListByDate(date);
        for( int i = 0 ; i < bonusList.size() ; i++) {
            Bonus bonus = bonusList.get(i);
            //calculate total to Account
            Account account = accountService.selectAccountByUserID(bonus.getUserID());
            if (account==null){
                Account newaccount = new Account();
                newaccount.setUserID(bonus.getUserID());
                newaccount.setTotalBonus(bonus.getBonusAmt());
                newaccount.setAvailBonus(bonus.getBonusAmt());
                newaccount.setWithdrawPercent(0);
                newaccount.setStatus(0);
                newaccount.setTotalOrderAmt(0);
                accountService.addNewAccount(newaccount);
            } else {
                double newTotalAmt = account.getTotalBonus()+bonus.getBonusAmt();
                account.setTotalBonus(newTotalAmt);
                if (bonus.getBonusType().equalsIgnoreCase("fund")) {
                    //ignore fund bonus
                } else {
                    double newAvailAmt = account.getAvailBonus()+bonus.getBonusAmt();
                    account.setAvailBonus(newAvailAmt);
                }
                logger.info("update Account information: " + account.getAccountID() + "to amount:" + account.getAvailBonus());
                accountService.updateAccount(account);
            }
        }
        return ResultService.success();
    }

    /*
    1.获取当日奖金列表，更新每条奖金的金额到对应的汇总表ttbonus；
    2.获取当日订单列表，将订单金额更新到对应的汇总表ttorder；
     */
    @RequestMapping("/summaryUpdate")
    public Result summaryUpdate(){
        Date date = new Date();
        List<Bonus> bonusList = bonusService.bonusListByDate(date);
        for( int i = 0 ; i < bonusList.size() ; i++) {
            Bonus bonus = bonusList.get(i);
            if (bonus.getBonusType().equals("discount")) continue;
              //calculate daily summary to summary, the records must existing by summaryUserOrder
            System.out.println(bonus.getBonusAmt());
            Summary summary = summaryService.selectTodaySummarybyID(bonus.getUserID());
            double newAmount;
            newAmount = summary.getTtbonus();
            newAmount= newAmount + bonus.getBonusAmt();
            summary.setTtbonus(newAmount);
            logger.info("Update to user id "+ summary.getUserID() +"new Ttbonus amount "+ summary.getTtbonus());
            summaryService.updateSummary(summary);
        }
        List<UserOrder> userOrderList = userOrderService.orderListByDate(date);
        for( int i = 0 ; i < userOrderList.size() ; i++) {
            UserOrder userOrder = userOrderList.get(i);
            Summary summary = summaryService.selectTodaySummarybyID(userOrder.getUserId());
            double newAmount;
            newAmount = summary.getTtorder();
            newAmount= newAmount + userOrder.getAmount();
            summary.setTtorder(newAmount);
            logger.info("Update to user id "+ summary.getUserID() +"new ttorder amount "+ summary.getTtorder());
            summaryService.updateSummary(summary);
        }
        return ResultService.success();
    }

    /*
    1.获取当日汇总列表；
    2.每一个汇总列表，二叉树左顺序递归遍历，获取用户的团队列表sgzUtil.getGroupList；
    3.循环获取的团队列表，直接去汇总表里获取每个人的当日奖金ttbonus,然后累加起来放入Teamttbonus；
    4.用团队累计奖金处于团队总业绩，计算出团队当日奖金发放比例。
     */
    @RequestMapping("/summaryUpdateTodayTeamBonus")
    public Result summaryUpdateTodayTeamBonus() {
        List<Summary> summaryList = summaryService.selectTodaySummaryList(1, 2000);
        for (int i = 0; i < summaryList.size(); i++) {
            Summary summary = summaryList.get(i);
            Double teamttbonus = summary.getTeamttbonus();
            ArrayList<User> userList = new ArrayList<User>();
            userList = sgzUtil.getGroupList(summary.getUserID());
            for(int j = 0; j < userList.size(); j++) {
                User member = userList.get(j);
                Summary memberSummary = summaryService.selectTodaySummarybyID(member.getUserId());
                teamttbonus = teamttbonus + memberSummary.getTtbonus();
            }
            summary.setTeamttbonus(teamttbonus);
            Double percentage;
            if (summary.getTeamttorder()!=0){
                percentage = teamttbonus/summary.getTeamttorder();
                int temp = (int)(percentage * 10000);
                percentage = (double)temp / 100;
                summary.setTeampercent(percentage);
            }
            logger.info("Update to user id "+ summary.getUserID() +"new Team ttnonus "+ summary.getTeamttbonus());
            summaryService.updateSummary(summary);
        }
        return ResultService.success();
    }

    /*
    1.获取非叶子节点的用户列表，叶子节点的用户已经默认设置为1；
    2.调用左序递归算法程序sgzUtil.getGroupList获取对应的团队列表；
    3.将团队人数更新到对应的用户表团队人数里。
     */
    @RequestMapping("/updateteamsize")
    public Result updateteamsize(){
        Date date = new Date();
        List<User> userList = userService.userListNonLeaf();
        for( int i = 0 ; i < userList.size() ; i++) {
            User user = userList.get(i);
            ArrayList userTeam = sgzUtil.getGroupList(user.getUserId());
            user.setTeampplNum(userTeam.size());
            logger.info("Update to user id "+ user.getUserId() +"new Teamsize "+ user.getTeampplNum());
            userService.updateUser(user);
        }
        return ResultService.success();
    }

    /*
    1.根据用户团队业绩以及子节点级别，更新用户的级差；
    2.根据用户团队业绩，更新用户的福利基金级别；
     */
    @RequestMapping("/updateGradeAndFund")
    public Result updateGradeAndFund(){
        Date date = new Date();
        List<User> userList = userService.userList();
        //从子叶子节点开始检测分级
        for( int i = (userList.size()-1) ; i >=0 ; i--) {
            User user = userList.get(i);
            boolean needUpdate = false;
            if (user.getGrade()==0) {
                if (user.getTeamtotal() < 100000) {
                    // continue;
                } else if (user.getTeamtotal() >= 100000) {
                    user.setGrade(6);
                    needUpdate = true;
                }
            } else if (user.getGrade()==6) {
                if ((user.getTreeLeft()!=0)&&(user.getTreeRight()!=0)){
                    Integer leftgrade = userService.selectbyUserId(user.getTreeLeft()).getGrade();
                    Integer rightgrade = userService.selectbyUserId(user.getTreeRight()).getGrade();
                    if ((leftgrade == 6) && (rightgrade == 6)) {
                        user.setGrade(7);
                        needUpdate = true;
                    }
                }
            } else if (user.getGrade()==7){
                if ((user.getTreeLeft()!=0)&&(user.getTreeRight()!=0)) {
                    Integer leftgrade = userService.selectbyUserId(user.getTreeLeft()).getGrade();
                    Integer rightgrade = userService.selectbyUserId(user.getTreeRight()).getGrade();
                    if ((leftgrade == 7) && (rightgrade == 7)) {
                        user.setGrade(8);
                        needUpdate = true;
                    }
                }
            } else if (user.getGrade()==8){
                if ((user.getTreeLeft()!=0)&&(user.getTreeRight()!=0)) {
                    Integer leftgrade = userService.selectbyUserId(user.getTreeLeft()).getGrade();
                    Integer rightgrade = userService.selectbyUserId(user.getTreeRight()).getGrade();
                    if ((leftgrade == 8) && (rightgrade == 8)) {
                        user.setGrade(9);
                        needUpdate = true;
                    }
                }
            } else if (user.getGrade()==9){
                if ((user.getTreeLeft()!=0)&&(user.getTreeRight()!=0)) {
                    Integer leftgrade = userService.selectbyUserId(user.getTreeLeft()).getGrade();
                    Integer rightgrade = userService.selectbyUserId(user.getTreeRight()).getGrade();
                    if ((leftgrade == 9) && (rightgrade == 9)) {
                        user.setGrade(10);
                        needUpdate = true;
                    }
                }
            } else if (user.getGrade()==10){
                //TBC by user
            } else {
                //TBC by user
            }

            if (user.getFund()==0){
                if (user.getTeamtotal()>=1000000){
                    user.setFund(11);
                    needUpdate = true;
                }
            } else if (user.getFund()==11){
                if (user.getTeamtotal()>=3000000){
                    user.setFund(12);
                    needUpdate = true;
                }
            } else if (user.getFund()==12){
                if (user.getTeamtotal()>=5000000){
                    user.setFund(13);
                    needUpdate = true;
                }
            } else if (user.getFund()==13){
                //TBC by user
            } else {
                //TBC by user
            }

            if (needUpdate){
                logger.info("Update to user id "+ user.getUserId() +"new grade "+ user.getGrade() + " or fund " + user.getFund());
                userService.updateUser(user);
            }
        }
        return ResultService.success();
    }

    public Date CurrentDate(){
        Date date = new Date();
            /*
            Calendar Cal=Calendar.getInstance();
            Cal.setTime(date);
            Cal.add(HOUR_OF_DAY,14);
            date = Cal.getTime();
            System.out.println(Cal);
            System.out.println(date);
            */
        return date;
    }
}
