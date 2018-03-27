package com.wolves.admin.test;

/**
 * Created with IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-1-17
 * Time: 下午8:16
 */
public class TestSession {

    private static final String IP = "110.76.38.169";
    private static final int PORT = 8000;

    public static void main(String[] args) throws Exception {
      /*  SessionCommon.DafSession dafSession = putSession("", 1, "helloworld".getBytes());
        System.out.println(dafSession.getSessionId());
        System.out.println(new String(getSession(dafSession.getSessionId()).getContent().toByteArray()));*/
    }


  /*  *//**
     * 获取session
     *
     * @param sessionId 会话id
     * @return daf会话
     * @throws Exception 异常
     *//*
    private static SessionCommon.DafSession getSession(String sessionId) throws Exception {
        if (StringUtils.isNotBlank(sessionId)) {
            SessionCom.GetSessionRequest.Builder reqBuilder = SessionCom.GetSessionRequest.newBuilder();
            reqBuilder.setBaseRequest(GenerateBaseRequest.generate("ADMIN", "0.0.1", "getsession", "0.0.1"));
            reqBuilder.setSessionId(sessionId);
            SessionCom.GetSessionResponse response = SessionCom.GetSessionResponse.parseFrom(
                    CommManager.sendForTestOnly(IP, PORT, reqBuilder.build().toByteArray())
            );
            return response.getDafSession();
        } else {
            return null;
        }
    }

    *//**
     * 存储session
     *
     * @param sessionId     会话id
     * @param timeoutInSecs 会话有效时长, 单位(秒)
     * @param content       会话内容
     * @return daf会话
     * @throws Exception 异常
     *//*
    private static SessionCommon.DafSession putSession(String sessionId, long timeoutInSecs, byte[] content) throws Exception {
        SessionCom.PutSessionRequest.Builder reqBuilder = SessionCom.PutSessionRequest.newBuilder();
        reqBuilder.setBaseRequest(GenerateBaseRequest.generate("ADMIN", "0.0.1", "putsession", "0.0.1"));
        reqBuilder.setSessionId(sessionId).setTimeoutInSecs(timeoutInSecs).setContent(ByteString.copyFrom(content));
        SessionCom.PutSessionResponse response = SessionCom.PutSessionResponse.parseFrom(
                CommManager.sendForTestOnly(IP, PORT, reqBuilder.build().toByteArray())
        );
        return response.getDafSession();
    }
*/
}
