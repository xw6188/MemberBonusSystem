package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.ListObject;
import com.ben.sgzspringbootapi.entity.PageResult;
import com.ben.sgzspringbootapi.entity.Result;
import com.ben.sgzspringbootapi.entity.ResultEnum;

import java.util.List;

public class ResultService {
    public static Result success(Object object){
        Result result = new Result();
        result.setCode(ResultEnum.OK.getCode());
        result.setMsg(ResultEnum.OK.getMsg());
        if (object!=null){
            if (object instanceof List){
                ListObject listObject = new ListObject();
                listObject.setRecord(object);
                List temp = (List)object;
                listObject.setTotalCount(temp.size());
                result.setData(listObject);
            } else{
                result.setData(object);
            }
        } else {
            result.setData(null);
        }
        return result;
    }

    public static Result success(){
        Result result = new Result();
        result.setCode(ResultEnum.OK.getCode());
        result.setMsg(ResultEnum.OK.getMsg());
        return result;
    }

    public static Result error(ResultEnum resultEnum){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        return result;
    }

    public static Result error(String string){
        Result result = new Result();
        result.setCode(ResultEnum.ERROR.getCode());
        result.setMsg(ResultEnum.ERROR.getMsg());
        result.setData(string);
        return result;
    }
    public static Result success( PageResult pageResult){
        Result result = new Result();
        result.setCode(ResultEnum.OK.getCode());
        result.setMsg(ResultEnum.OK.getMsg());
        if (pageResult!=null){
            result.setData(pageResult);
        }else {
            result.setData(null);
        }

        return result;
    }
}
