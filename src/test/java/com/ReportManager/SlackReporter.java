package com.ReportManager;

import java.io.File;
import java.util.Arrays;
import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.files.FilesUploadRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class SlackReporter {


  public void send_Message_To_Channel(String message, String channel) {
    try {
      String authToken = System.getenv("SLACK_AUTH");
      Slack slack = Slack.getInstance(get_Config());

      MethodsClient methods = slack.methods(authToken);

      ChatPostMessageRequest request =
          ChatPostMessageRequest.builder().channel(channel).text(message).build();

      methods.chatPostMessage(request);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void send_Failure_Data_To_Channel(String message, String ssPath, String channel) {
    try {
      String authToken = System.getenv("SLACK_AUTH");
      Slack slack = Slack.getInstance(get_Config());
      MethodsClient methods = slack.methods(authToken);

      ChatPostMessageRequest request =
          ChatPostMessageRequest.builder().channel(channel).text(message).build();

      ChatPostMessageResponse response = methods.chatPostMessage(request);

      if (ssPath != null) {
        FilesUploadRequest filerequest =
            FilesUploadRequest.builder().channels(Arrays.asList(channel)).file(new File(ssPath))
                .filename("FailureSS").threadTs(response.getTs()).build();

        methods.filesUpload(filerequest);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private SlackConfig get_Config() {
    SlackConfig config = new SlackConfig();
    String proxy_ip = System.getenv("PROXY_IP");
    String proxy_port = System.getenv("PROXY_PORT");
    config.setProxyUrl("http://" + proxy_ip + ":" + proxy_port);
    return config;
  }

}
