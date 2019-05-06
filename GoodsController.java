package com.jk.controller;

import com.alibaba.fastjson.JSON;
import com.jk.bean.Pl;
import com.jk.service.GoodsService;
import com.jk.util.MenuTree;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private MongoTemplate mongo;
    @Autowired
    private JedisPool je;
    @Autowired
    private GoodsService service;
    @RequestMapping("/getTree")
    @ResponseBody
    public List<MenuTree> getTree(){
        Jedis resource = je.getResource();


        String string2 = resource.get("wysptree");
        List<MenuTree> parseArray = JSON.parseArray(string2, MenuTree.class);
        if(StringUtils.isNotEmpty(string2)){
            return parseArray;
        }else{
            List<MenuTree> list=service.getTree();
            String string = JSON.toJSON(list).toString();
            resource.set("wysptree", string);
            String string3 = resource.get("wysptree");
            List<MenuTree> parseArray2 = JSON.parseArray(string3, MenuTree.class);
            return parseArray2;
        }
    }
    @RequestMapping("toindex")
    public String toindex(){
        return "index";
    }
    @RequestMapping("tofind")
    public String tofind(){
        return "find";
    }
    @RequestMapping("findgoods")
    @ResponseBody
    public HashMap<String,Object> goodsfind(Integer start,Integer pageSize){

        return service.getgoods(start,pageSize);
    }
    @RequestMapping("toshow")
    public ModelAndView toshow(String id){
        ModelAndView mv = new ModelAndView();
        mv.addObject("id",id);
        mv.setViewName("show");
        return mv;

    }
    @RequestMapping("querypl")
    @ResponseBody
    public HashMap<String,Object> query(String id,Integer start,Integer pageSize){
        return service.query(id,start,pageSize);
    }
    @RequestMapping("toaddpl")
    public String toadddl(){
        return "adddl";
    }
    @RequestMapping("adddl")
    @ResponseBody
    public String adddl(Pl pl){
        pl.setCommentDate(new Date());
        service.addpl(pl);
        return null;
    }
}
