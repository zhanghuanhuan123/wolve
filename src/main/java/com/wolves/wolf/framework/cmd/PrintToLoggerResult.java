package com.wolves.wolf.framework.cmd;


import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import com.opensymphony.xwork.util.TextParseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PrintToLoggerResult
        implements Result {
    private static Logger logger = LogManager.getLogger(PrintToLoggerResult.class);
    private String param;

    public String getParam() {
        return this.param;
    }


    public void setParam(String param) {
        this.param = param;
    }


    public void execute(ActionInvocation actionInvocation) throws Exception {
        String result = TextParseUtil.translateVariables(this.param, actionInvocation.getStack());
        logger.info("Cost: " + result);
    }

}
