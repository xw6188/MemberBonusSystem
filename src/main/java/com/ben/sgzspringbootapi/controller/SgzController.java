package com.ben.sgzspringbootapi.controller;

import com.ben.sgzspringbootapi.entity.*;
import com.ben.sgzspringbootapi.service.*;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.javafx.binding.StringFormatter;
import org.jasypt.encryption.StringEncryptor;
import org.jetbrains.annotations.NotNull;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import sun.rmi.server.InactiveGroupException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Calendar.HOUR_OF_DAY;

//会员分销系统APP，商城与后台网站管理系统接口
@RestController
@RequestMapping("/api")
public class SgzController {

    private Logger logger = LoggerFactory.getLogger(SgzController.class);

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

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private UserOrderDetailService userOrderDetailService;

    @Autowired
    StringEncryptor encryptor;

    //测试接口服务正在运行
    @RequestMapping("/ping")
    public Result ping() {
        logger.info("Someone is pinging the the server");
        return ResultService.success("API is running");
    }

    //获取会员信息
    @RequestMapping("/getUser/{id}")
    public Result GetUser(@PathVariable int id) {
        if (userService.selectbyUserId(id) == null) {
            logger.warn("User infomation of ID not found: " + id);
            return ResultService.error(ResultEnum.USER_NOT_EXIST);
        } else {
            logger.info("Getting User information by " + id);
            return ResultService.success(userService.selectbyUserId(id));
        }
    }

    //获取会员列表
    @RequestMapping("/userList")
    public Result UserList() {
        logger.info("Getting User List");
        return ResultService.success(userService.userList());
    }

    //获取会员列表账号
    @RequestMapping("/userAccountList")
    public Result UserAccountList(@RequestParam(required = false, defaultValue = "0") Integer page,
                                  @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        logger.info("Getting User Account List");
        return ResultService.success(userAccountService.userAccountList(page, pageSize));
    }

    /* @RequestMapping(value = "/userAccountListSearch", method = RequestMethod.POST, consumes = "application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
     public Result userAccountListSearch(@RequestBody User user){
         return ResultService.success(userAccountService.userAccountListSearch(user));
     }*/
    //会员搜索
    @GetMapping(value = "/userAccountListSearch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result userAccountLbonusListByUserIDSearchistSearch(@RequestParam(required = false, defaultValue = "0") Integer userId, String mobile, String nickName,
                                                               @RequestParam(required = false, defaultValue = "0") Integer level,
                                                               @RequestParam(required = false, defaultValue = "0") Integer page,
                                                               @RequestParam(required = false, defaultValue = "20") Integer pageSize) {


        try {
            logger.info("Searching the user account list with these information: userID " + userId + "mobile " + mobile + "nickName "+nickName + "level " + level);
            List<UserAccount> userAccounts = userAccountService.userAccountListSearch(userId, mobile, nickName, level, page, pageSize);
            ListPageUtil<UserAccount> list = new ListPageUtil<>(userAccounts, page, pageSize);
            return ResultService.success(new PageResult(list.getTotalCount(), list.getPagedList()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultService.error("格式错误，请输入数字");
        }

    }

    //会员提现记录
    @RequestMapping("/withdrawList/{userID}")
    public Result WithdrawList(@PathVariable int userID) {
        if (userService.selectbyUserId(userID) == null) {
            logger.warn("User withdraw list not found:" + userID);
            return ResultService.error(ResultEnum.USER_NOT_EXIST);
        } else {
            logger.info("Listing user withdraw list: " + userID);
            return ResultService.success(withdrawService.withdrawListByUserID(userID));
        }
    }

    //后台管理，系统所有提现记录
    @RequestMapping("/withdrawListAll")
    public Result WithdrawListAll(Integer page, Integer pageSize) {
        logger.info("List all the withdraw List in the system");
        return ResultService.success(withdrawService.withdrawListAll(page == null ? 1 : page, pageSize == null ? 20 : pageSize));
    }

    //删除用户
    @RequestMapping("/deleteUser/{id}")
    public Result deleteUser(@PathVariable int id) {
        if (userService.selectbyUserId(id) != null) {
            logger.info("Deleting the user: " + id);
            userService.deletebyUserId(id);
            return ResultService.success();
        } else {
            logger.warn("Can't find the user ID when try do delete it:" + id);
            return ResultService.error(ResultEnum.USER_NOT_EXIST);
        }
    }

    //更新用户信息
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, consumes = "application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result updateUser(@RequestBody User user) {
        User existinguser = userService.selectbyUserId(user.getUserId());
        if (existinguser == null) {
            logger.warn("Can't find the user infomation when try to updateUser:" + user.getUserId());
            return ResultService.error(ResultEnum.USER_NOT_EXIST);
        } else {
            //只支持更新4种用户信息，其中级别信息更新只开放给后台管理系统
            logger.info("Updating user: " + existinguser.getUserId());
            if ((user.getMobile() != null) && (existinguser.getMobile() != user.getMobile())) {
                logger.info("Mobile changed from "+ existinguser.getMobile()+" to "+user.getMobile());
                existinguser.setMobile(user.getMobile());
            }
            if ((user.getNickName() != null) && (existinguser.getNickName() != user.getNickName())) {
                logger.info("NickName changed from " + existinguser.getNickName() + " to " + user.getNickName());
                existinguser.setNickName(user.getNickName());
            }
            if ((user.getUserName() != null) && (existinguser.getUserName() != user.getUserName())) {
                logger.info("UserName changed from " + existinguser.getUserName() + " to " + user.getUserName());
                existinguser.setUserName(user.getUserName());
            }
            if ((user.getLevel() != null) && (existinguser.getLevel() != user.getLevel())) {
                logger.info("The user level updated from " + existinguser.getLevel() + " to " + user.getLevel() + "and the updated times set to 2, can't auto upgrade anymore");
                existinguser.setLevel(user.getLevel());
                existinguser.setUpgrade(2);
            }
            userService.updateUser(existinguser);
            return ResultService.success(ResultEnum.OK);
        }
    }

    //用户申请提现, 最小提现金额100
    @RequestMapping(value = "/requestWithdraw", method = RequestMethod.POST, consumes = "application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result requestWithdraw(@RequestBody Withdraw withdraw) {
        User tempUser = userService.selectbyUserId(withdraw.getUserID());
        if (tempUser == null) {
            logger.warn("User information not found when try to Withdraw " + withdraw.getUserID());
            return ResultService.error(ResultEnum.USER_NOT_EXIST);
        } else if ((accountService.selectAccountByUserID(withdraw.getUserID()).getAvailBonus()) < withdraw.getAmount()) {
            logger.warn("The withdraw amount excceed limit for " + withdraw.getUserID());
            return ResultService.error(ResultEnum.EXCCEED_ACCOUNT);
        } else if (withdraw.getAmount() < 100) {
            logger.warn("The withdraw amount at least be 100");
            return ResultService.error(ResultEnum.SMALL_ACCOUNT);
        } else {
            withdraw.setMobile(tempUser.getMobile());
            if (withdraw.getAccountName() == null) {
                withdraw.setAccountName(tempUser.getUserName());
            }
            double tempamt = withdraw.getAmount() * 0.08;
            String shouxufei = "提现需要扣除" + tempamt + "PV的手续费（提现金额的8%）";
            withdraw.setWithdrawDetail(shouxufei);
            logger.info("writing the withdraw request" + withdraw.getWithdrawID());
            withdrawService.requestWithdraw(withdraw);
            return ResultService.success();
        }
    }


    //更新提现状态，用于后台审核提现
    @RequestMapping(value = "/updateWithdrawStatus", method = RequestMethod.POST, consumes = "application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result updateWithdrawStatus(@RequestBody Withdraw withdraw) {
        if (userService.selectbyUserId(withdraw.getUserID()) == null) {
            logger.warn("User infomation of ID not found when try to approve withdraw: " + withdraw.getUserID());
            return ResultService.error(ResultEnum.USER_NOT_EXIST);
        } else if (withdrawService.selectWithdrawByID(withdraw.getWithdrawID()) == null) {
            logger.warn("Withdraw record can't be found during update: " + withdraw.getWithdrawID());
            return ResultService.error(ResultEnum.WITHDRAW_NOT_EXIST);
        } else {
            if (withdraw.getStatus().equalsIgnoreCase("审核通过")) {
                Account tempAccount = accountService.selectAccountByUserID(withdraw.getUserID());
                double tempAvaiBonus = tempAccount.getAvailBonus() - withdrawService.selectWithdrawByID(withdraw.getWithdrawID()).getAmount();
                tempAccount.setAvailBonus(tempAvaiBonus);
                accountService.updateAccount(tempAccount);
                //审核通过之后将提现费用拆分成92%到账费用和8%的手续费（爱心基金3%， 网络管理费5%）
                Withdraw tempWithdraw = withdrawService.selectWithdrawByID(withdraw.getWithdrawID());
                double tempAmount = tempWithdraw.getAmount();
                tempWithdraw.setAmount(tempAmount * 0.92);
                tempWithdraw.setWithdrawDetail(tempWithdraw.getWithdrawDetail() + withdraw.getWithdrawDetail());
                tempWithdraw.setStatus(withdraw.getStatus());
                logger.info("Updating withdraw information " + tempWithdraw.getWithdrawID());
                withdrawService.updateWithdraw(tempWithdraw);
                /*
                tempWithdraw.setAmount(tempAmount * 0.08);
                tempWithdraw.setWithdrawDetail("提现ID:"+tempWithdraw.getWithdrawID()+"扣除"+"8%的手续费");
                tempWithdraw.setStatus(withdraw.getStatus());
                withdrawService.requestWithdraw(tempWithdraw);
                */
                return ResultService.success(ResultEnum.OK);
            } else {
                Withdraw tempWithdraw = withdrawService.selectWithdrawByID(withdraw.getWithdrawID());
                double tempAmount = tempWithdraw.getAmount();
                tempWithdraw.setWithdrawDetail(withdraw.getWithdrawDetail());
                tempWithdraw.setStatus(withdraw.getStatus());
                withdrawService.updateWithdraw(tempWithdraw);
                logger.info("Updating withdraw information " + tempWithdraw.getWithdrawID());
                return ResultService.success(ResultEnum.OK);
            }
        }
    }

    //添加会员
    @RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes = "application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result register(@RequestBody User user) {
        if (userService.selectbyUserId(user.getUserId()) != null) {
            logger.warn("User Already exist when try to add user: " + user.getUserId());
            return ResultService.error(ResultEnum.USER_EXISTS);
        } else {
            if (user.getParent() != null) {
                logger.warn("User parent Code is not null" + user.getUserId());
                return ResultService.error("parentCode must be null");
            }
            if (user.getTreePosition() != null) {
                logger.warn("User Positon is not null" + user.getUserId());
                return ResultService.error("treePosition must be null");
            }
            if (user.getNominator() == 0) {
                //只有第一个会员，二叉树根节点才使用这段初始化逻辑
                user.setParent(0);
                user.setTreePosition(1);
                user.setTreeLeft(0);
                user.setTreeRight(0);
                user.setLevel(0);
                user.setUpgrade(0);
                user.setTeampplNum(1);
                user.setGrade(0);
                user.setFund(0);
            } else {
                //根据传入的推荐人位置来递归计算所处的二叉树子节点位置位置
                User nomiUser = userService.selectbyUserId(user.getNominator());
                Integer position;

                //推荐人必须存在
                if (nomiUser == null) {
                    logger.warn("Nominator can't be null");
                    return ResultService.error("Can't find valid Nominate User");
                } else {
                    //根据自动滑落规则递归查找所属位置
                    position = findParentPositionByNominator(nomiUser, nomiUser);
                    if (position == 0) {
                        return ResultService.error("There is problem for the tree leaf, please verify and check again!");
                    }
                }
                if (position % 2 == 0) {
                    //如果所放位置为父节点的左手边，更新父节点位置和ID
                    User parent = userService.selectbyPosition(position / 2);
                    parent.setTreeLeft(user.getUserId());
                    userService.updateUser(parent);
                    user.setParent(parent.getUserId());
                } else {
                    //如果所放位置为父节点的右手边，更新父节点的位置和ID
                    User parent = userService.selectbyPosition((position - 1) / 2);
                    parent.setTreeRight(user.getUserId());
                    userService.updateUser(parent);
                    user.setParent(parent.getUserId());
                }
                user.setTreePosition(position);
                user.setTreeLeft(0);
                user.setTreeRight(0);
                user.setLevel(0);
                user.setUpgrade(0);
                user.setTeampplNum(1);
                user.setGrade(0);
                user.setFund(0);
            }
            //写入新会员信息
            logger.info("adding new user infomation: " + user.getUserId());
            userService.addNewUser(user);
            Account newaccount = new Account();
            newaccount.setUserID(user.getUserId());
            newaccount.setTotalBonus(0);
            newaccount.setAvailBonus(0);
            newaccount.setWithdrawPercent(0);
            newaccount.setStatus(0);
            newaccount.setTotalOrderAmt(0);
            //写入新会员账户信息
            logger.info("add new account information: " + newaccount.getUserID());
            accountService.addNewAccount(newaccount);
            return ResultService.success();

        }
    }

    //获取订单信息
    @RequestMapping("/getOrder/{orderId}")
    public Result selectOrder(@PathVariable String orderId) {
        UserOrder userOrder = userOrderService.selectOrder(orderId);
        if (userOrder == null) {
            logger.warn("The user order can't be find:" + orderId);
            return ResultService.error(ResultEnum.ORDER_NOT_EXIST);
        }else {
            logger.info("Getting user order information: " + orderId);
            return ResultService.success(userOrder);
        }
    }

    //更新会员名称
    @RequestMapping(value = "/updateUserName", method = RequestMethod.GET)
    public Result update(@RequestParam("id") Integer id, @RequestParam("name") String name) {
        userService.updateUserName(id, name);
        return ResultService.success();
    }

    //添加订单
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST, consumes = "application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result newOrder(@RequestBody UserOrder userOrder) {
        //UserOrder userOrder = new UserOrder(10002, 416, 1000.0);
        if (userService.selectbyUserId(userOrder.getUserId()) == null) {
            logger.warn("User do not exist when try to add user order: " + userOrder.getOrderId());
            return ResultService.error(ResultEnum.USER_NOT_EXIST);
        }

        if (accountService.selectAccountByUserID(userOrder.getUserId()) == null) {
            logger.warn("User Account do not exist when try to add user order: " + userOrder.getOrderId());
            return ResultService.error(ResultEnum.ACCOUNT_NOT_EXIST);
        }

        if (userOrderService.selectOrder(userOrder.getOrderId()) != null) {
            logger.warn("User Order Already exists: " + userOrder.getOrderId());
            return ResultService.error(ResultEnum.ORDER_EXISTS);
        } else {
            logger.info("Adding new userOrder " + userOrder.getOrderId());
            userOrderService.addNewUserOrder(userOrder);
            System.out.println(sgzUtil.CurrentDate());
            User user = userService.selectbyUserId(userOrder.getUserId());
            //订单金额需要跟新到所有父亲节点的团队信息里，作为对碰奖技术以及团队业绩
            logger.info("Now calculating DP Position & Team Amount & Today's Team Amount:" + user.getUserId());
            updateDPBasicTeamAmounts(user, userOrder.getAmount());
            Account account = accountService.selectAccountByUserID(userOrder.getUserId());
            Double tempTotalOrderAmt = account.getTotalOrderAmt() + userOrder.getAmount();
            account.setTotalOrderAmt(tempTotalOrderAmt);
            logger.info("Update Account information" + account.getUserID());
            accountService.updateAccount(account);
            if (userOrder.getDiscount() > 0) {
                if (userService.selectbyUserId(userOrder.getDiscount()) == null) {
                    return ResultService.error(ResultEnum.USER_NOT_EXIST);
                } else {
                    //折扣直接进入被折扣人奖金列表
                    Bonus bonus = new Bonus();
                    bonus.setUserID(userOrder.getDiscount());
                    bonus.setRelatedUserID(userOrder.getUserId());
                    bonus.setBonusType("discount");
                    bonus.setBonusAmt(userOrder.getAmount() * 3 / 100);
                    bonus.setBonusdetail("折扣补贴3% 的订单PV值。订单编号: " + userOrder.getOrderId());
                    bonus.setBonusDate(sgzUtil.CurrentDate());
                    bonusService.addNewBonus(bonus);
                }
            }
            return ResultService.success();
        }
    }

    //由叶子节点递归更新所有父亲节点到跟节点的金额信息
    private void updateDPBasicTeamAmounts(@NotNull User user, double orderAmount) {
        double newCalamount = user.getCalamount() + orderAmount;
        double newTeamtotal = user.getTeamtotal() + orderAmount;
        double newLastTeamamt = user.getLastteamamt() + orderAmount;
        user.setCalamount(newCalamount);
        user.setTeamtotal(newTeamtotal);
        user.setLastteamamt(newLastTeamamt);
        userService.updateUser(user);
        logger.info("recursive call to calculate the Amount" + user.getUserId());
        logger.info("Calculation Group Amounts: newCalamount " + newCalamount + "newTeamtotal：" + newTeamtotal + "newLastTeamamt:" + newLastTeamamt);

        if (user.getParent() == 0) {
            return;
        } else {
            updateDPBasicTeamAmounts(userService.selectbyUserId(user.getParent()), orderAmount);
        }
    }

    //删除订单
    @RequestMapping("/deleteOrder/{id}")
    public Result deleteOrder(@PathVariable String id) {
        if (userOrderService.selectOrder(id) == null) {
            logger.warn("Can't find the order when try to delete: " + id);
            return ResultService.error(ResultEnum.ORDER_NOT_EXIST);
        } else {
            logger.info("Deleting the order " + id);
            userOrderService.deleteOrder(id);
            return ResultService.success();
        }
    }

    /*
        private Integer findParentPositionByNominator(@NotNull User user){
            if (userService.selectbyPosition(user.getTreePosition()*2)==null){
                return user.getTreePosition()*2;

            } else {
                if (userService.selectbyPosition(user.getTreePosition()*2+1)==null){
                    return user.getTreePosition()*2+1;
                }
                else{
                    return findPositionByNominator(userService.selectbyPosition(user.getTreePosition() * 2));
                }
            }
        }
    */
    //通过推荐人位置递归查找直属父亲节点
    private Integer findParentPositionByNominator(@NotNull User user, User nomiUser) {
        logger.info("Finding Parent Position by user: " + user.getUserId() + " and Nominator" + nomiUser.getUserId());
        Integer leftUserID = user.getTreeLeft();
        Integer rightUserID = user.getTreeRight();
        User leftUser = userService.selectbyUserId(leftUserID);
        User rightUser = userService.selectbyUserId(rightUserID);
        //没有子叶子节点
        if ((leftUserID == 0) && (rightUserID == 0)) {
            //返回当前用户左子节点
            return user.getTreePosition() * 2;
        }

        if ((leftUserID != 0) && (rightUserID == 0)) {
            //如果只有左叶子节点
            //而且当前用户就是推荐用户
            if (user.getUserId() == nomiUser.getUserId()) {
                //那就返回推荐会员的右叶子节点位置
                return user.getTreePosition() * 2 + 1;
            } else {
                //自动滑落
                return findParentPositionByNominator(leftUser, nomiUser);
            }
        }

        if ((leftUserID == 0) && (rightUserID != 0)) {
            //默认都是先放最终的左节点，如果右节点先有数据，二叉树有问题需要检查原因。
            return 0;
        }

        if ((leftUserID != 0) && (rightUserID != 0)) {
            //如果两边叶子都有节点，根据小市场大边原理自动滑落,如果两边相等优先放在左边子节点
            if (leftUser.getTeamtotal() >= rightUser.getTeamtotal()) {
                return findParentPositionByNominator(leftUser, nomiUser);
            } else {
                //小市场大边在右子节点的时候，继续查找
                return findParentPositionByNominator(rightUser, nomiUser);
            }
        }
        return 0;
    }

    //获取会员级别信息
    @RequestMapping("/level")
    public Result levelList() {
        return ResultService.success(levelService.LevelList());
    }

    //更新会员级别信息
    @RequestMapping(value = "/updatelevel", method = RequestMethod.POST, consumes = "application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result updatelevel(@RequestBody Level level) {
        Level existingLevel = levelService.selectLevel(level.getLevelID());
        if (existingLevel == null) {
            logger.warn("level do not exist while try to update " + level.getLevelID());
            return ResultService.error(ResultEnum.LEVEL_NOT_EXIST);
        } else {
            logger.info("Updating level " + level.getLevelID());
            levelService.updateLevel(level);
            return ResultService.success(ResultEnum.OK);
        }
    }

    //获取会员账户信息
    @RequestMapping("/account/{userID}")
    public Result selectAccountByUserID(@PathVariable int userID) {
        if (accountService.selectAccountByUserID(userID) == null) {
            logger.warn("Account can't be found " + userID);
            return ResultService.error(ResultEnum.ACCOUNT_NOT_EXIST);
        } else {
            logger.info("Getting Account information " + userID);
            return ResultService.success(accountService.selectAccountByUserID(userID));
        }
    }

    //获取会员账户信息
    @RequestMapping(value = "/updateAccountInfo", method = RequestMethod.POST, consumes = "application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result updateAccountInfo(@RequestBody Account account) {
        Account existingAccount = accountService.selectAccountByUserID(account.getUserID());
        if (existingAccount == null) {
            logger.warn("Account can't be found during update" + account.getUserID());
            return ResultService.error(ResultEnum.ACCOUNT_NOT_EXIST);
        } else {
            if ((existingAccount.getAccountID() != null) && (existingAccount.getAccountID() != account.getAccountID())) {
                existingAccount.setAccountID(account.getAccountID());
            }
            if ((existingAccount.getAccountName() != null) && (existingAccount.getAccountName() != account.getAccountName())) {
                existingAccount.setAccountName(account.getAccountName());
            }
            logger.info("Updating Account information " + existingAccount.getAccountID());
            accountService.updateAccount(existingAccount);
            return ResultService.success(ResultEnum.OK);
        }
    }

    //查询奖金明细
    @RequestMapping("/selectBonus/{bonusID}")
    public Result bonusListByDate(@PathVariable int bonusID) {
        logger.info("Checking bonus detail: " + bonusID);
        return ResultService.success(bonusService.selectBonus(bonusID));
    }

    //查询当日的订单列表
    @RequestMapping("/orderListByDate")
    public Result orderListByDate() {
        logger.info("Returning the Order List Detail by Today");
        return ResultService.success(userOrderService.orderListByDate(sgzUtil.CurrentDate()));
    }

    //查询指定用户的订单列表
    @RequestMapping("/orderListByUserID/{userID}")
    public Result orderListByUserID(@PathVariable int userID) {
        logger.info("Checking the order list by user ID " + userID);
        //return ResultService.success(userOrderService.orderListByUserID(userID));
        return ResultService.success(userOrderDetailService.orderDetailListByUserID(userID));
    }

    //获取所有日结日结
    @RequestMapping("/summaryList")
    public Result summaryList() {
        logger.info("Getting the SummaryList");
        return ResultService.success(summaryService.summaryList());
    }

    //每日汇总分页
    @RequestMapping("/summaryListAll")
    public Result summaryListAll(@RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        logger.info("Getting the Summary List all");
        return ResultService.success(summaryService.summaryListAll(page, pageSize));
    }

    //获取每个用户日结
    @RequestMapping("/summaryListByUserID/{userID}")
    public Result summaryListByUserID(@PathVariable int userID, String startTime, String endTime,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        logger.info("Getting the summary List by user id");
        return ResultService.success(summaryService.selectSummarybyID(userID, page, pageSize, startTime, endTime));
    }

    //obsoleted
    @RequestMapping("/selectTodaySummaryList")
    public Result summaryListByUserID(Integer page, Integer pageSize) {
        return ResultService.success(summaryService.selectTodaySummaryList(page == null ? 1 : page, pageSize == null ? 20 : pageSize));
    }

    //obsoleted
    @RequestMapping("/selectYesterdaySummaryList")
    public Result summaryYesterdayListByUserID(@RequestParam(required = false, defaultValue = "1") Integer page,
                                               @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return ResultService.success(summaryService.selectYesterdaySummaryList(page, pageSize));
    }

    //获取用户奖金列表
    @RequestMapping("/bonusListByUserID/{userID}")
    public Result bonusListByUserID(@PathVariable int userID,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        logger.info("Getting the bonus List information " + userID);
        List<Bonus> list = bonusService.selectBonusByUserID(userID);
        ListPageUtil<Bonus> bonusList = new ListPageUtil<>(list, page, pageSize);
        return ResultService.success(new PageResult(bonusList.getTotalCount(), bonusList.getPagedList()));
    }

    //通过时间段查询奖金
    @RequestMapping("/bonusListByUserIDSearch/{userID}")
    public Result bonusListByUserIDSearch(@PathVariable int userID, String startTime, String endTime,
                                          @RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        logger.info("Getting the bonus List information search by period" + userID + startTime + endTime);
        List<Bonus> bonuses = bonusService.selectBonusByUserIDSearch(userID, startTime, endTime);
        ListPageUtil<Bonus> list = new ListPageUtil<>(bonuses, page, pageSize);

        return ResultService.success(new PageResult(list.getTotalCount(), list.getPagedList()));
    }

    //获取福利基金信息
    @RequestMapping("/bonusListByUserIDfund/{userID}")
    public Result bonusListByUserIDfund(@PathVariable int userID) {
        logger.info("Getting the fund bonus information " + userID);
        return ResultService.success(bonusService.selectBonusByUserIDfund(userID));
    }
//查看团队成员
    /*@RequestMapping("/userGroupMemberlist/{userID}")
    public Result getuserGroupMemberList(@PathVariable Integer userID)
    {
        User user = userService.selectbyUserId(userID);

        ArrayList<User> userList = new ArrayList<User>();
        userList = sgzUtil.getGroupList(userID);
        return ResultService.success(userList);
    }*/

    //查看团队成员分页
    @RequestMapping("/userGroupMemberlist/{userID}")
    public Result getuserGroupMemberList(@PathVariable Integer userID,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        logger.info("checking Usergroup member list " + userID);
        User user = userService.selectbyUserId(userID);

        ArrayList<User> userList = new ArrayList<User>();
        userList = sgzUtil.getGroupList(userID);
        ListPageUtil<User> list = new ListPageUtil<User>(userList, page, pageSize);

        return ResultService.success(new PageResult(list.getTotalCount(), list.getPagedList()));
    }

    //查看团队成员搜索
    @RequestMapping("/userGroupMemberlistSearch/{userID}")
    public Result getuserGroupMemberListSearch(@PathVariable Integer userID,
                                               @RequestParam(required = false, defaultValue = "1") Integer page,
                                               @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                               String mobile, String nickName, Integer level) {
        logger.info("checking Usergroup member list search " + userID);
        User user = userService.selectbyUserId(userID);

        ArrayList<User> userList = new ArrayList<User>();
        userList = sgzUtil.getGroupList(userID);

        if (mobile != null || nickName != null || level != null) {
            ArrayList<User> users = new ArrayList<>();
            for (int i = 0; i < userList.size(); i++) {
                User us = userList.get(i);
                if (mobile != null) {
                    if (mobile.getBytes().length < 11) {
                        return ResultService.error("请输入正确的手机号码");
                    }
                    if (us.getMobile().equalsIgnoreCase(mobile)) {
                        users.add(us);
                        continue;
                    }
                }
                if (nickName != null) {

                    if (us.getNickName().indexOf(nickName) != -1) {
                        users.add(us);
                        if (level != null && !us.getLevel().equals(level)) {
                            users.remove(users.size() - 1);
                        }
                        continue;
                    }

                }
                if (level != null) {

                    if (level.equals(0) || us.getLevel().equals(level)) {
                        users.add(us);
                    }
                }
            }
            if (users.size() > 0) {
                ListPageUtil<User> list = new ListPageUtil<User>(users, page, pageSize);
                return ResultService.success(new PageResult(list.getTotalCount(), list.getPagedList()));
            }
            return ResultService.success(new PageResult(0, null));
        } else {
            ListPageUtil<User> list = new ListPageUtil<User>(userList, page, pageSize);
            return ResultService.success(new PageResult(list.getTotalCount(), list.getPagedList()));
        }

    }
}
