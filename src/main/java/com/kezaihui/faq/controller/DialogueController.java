package com.kezaihui.faq.controller;

import com.kezaihui.faq.config.DialogueConfig;
import com.kezaihui.faq.controller.viewObject.DialogueResultVO;
import com.kezaihui.faq.dataObject.DialogueStatus;
import com.kezaihui.faq.response.CommonReturnType;
import com.kezaihui.faq.response.ResultData;
import com.kezaihui.faq.service.DialogueService;
import com.kezaihui.faq.util.RedisUtil;
import com.kezaihui.faq.vo.AnswerResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Author: lerry_li
 * @CreateDate: 2021/01/17
 * @Description
 */
@Api(tags = "对话")
@RestController
@RequestMapping("/dialogue")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")   //处理跨域请求
@Slf4j
public class DialogueController {
    @Autowired
    private DialogueConfig dialogueConfig;

    @Autowired
    private DialogueService dialogueService;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("提问问题")
    @RequestMapping(value = "/ask", method = RequestMethod.GET)
    public CommonReturnType ask(
            @ApiParam("用户问题") @RequestParam(name = "question") String question,
            @ApiParam("用户id") @RequestParam(name = "user_id") Integer userId) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        DialogueStatus statusModel = new DialogueStatus();
        //没有则为用户创建一个对话状态
        statusModel.setUserId(userId);
        //有则更新问题和robotId
        statusModel.setQuestion(question);
        //调用service回答
        statusModel = dialogueService.answer(statusModel);
        //创建视图对象
        DialogueResultVO vo = new DialogueResultVO();
        BeanUtils.copyProperties(statusModel, vo);
        stopWatch.stop();
        log.info("(userId={})当前用户提问\"{}\"，处理耗时{}ms", userId, question, stopWatch.getTotalTimeMillis());

        return CommonReturnType.create(vo, statusModel.getCodeMsg());
    }


    @ApiOperation("提问问题")
    @RequestMapping(value = "/ask", method = RequestMethod.GET)
    @ResponseBody
    public ResultData ask(
            @ApiParam("用户问题") @RequestParam(name = "question") String question,
            @ApiParam("用户id") @RequestParam(name = "user_id") Integer userId) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        DialogueStatus statusModel = new DialogueStatus();
        //没有则为用户创建一个对话状态
        statusModel.setUserId(userId);
        //有则更新问题和robotId
        statusModel.setQuestion(question);
        //调用service回答
        statusModel = dialogueService.answer2(statusModel);
        //创建视图对象
        DialogueResultVO vo = new DialogueResultVO();
        BeanUtils.copyProperties(statusModel, vo);
        stopWatch.stop();
        log.info("(userId={})当前用户提问\"{}\"，处理耗时{}ms", userId, question, stopWatch.getTotalTimeMillis());
        AnswerResultVo result = AnswerResultVo.builder().build();

        return ResultData.<AnswerResultVo>success()
                .data(result)
                .build();
    }

}