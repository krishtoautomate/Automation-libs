package com.ReportManager;

import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.files.FilesUploadRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

public class SlackReporter {

  private static Logger log = Logger.getLogger(SlackReporter.class.getName());

  public void send_Message_To_Channel(String message, String channels) {
    try {
      String authToken = System.getenv("SLACK_AUTH");
      List<String> recipients = Arrays.asList(channels.split("\\s*,\\s*"));
      for (String recipient : recipients) {
        Slack slack = Slack.getInstance(get_Config());

        MethodsClient methods = slack.methods(authToken);

        ChatPostMessageRequest request =
            ChatPostMessageRequest.builder().channel(recipient).text(message).build();

        methods.chatPostMessage(request);
      }
    } catch (Exception e) {
      log.info("Could not send message to slack channel due to: " + e.getLocalizedMessage());
    }

  }

  public void send_Failure_Data_To_Channel(String message, String ssPath, String channels) {
    try {
      String authToken = System.getenv("SLACK_AUTH");
      List<String> recipients = Arrays.asList(channels.split("\\s*,\\s*"));
      for (String recipient : recipients) {
        Slack slack = Slack.getInstance(get_Config());
        MethodsClient methods = slack.methods(authToken);

        ChatPostMessageRequest request =
            ChatPostMessageRequest.builder().channel(recipient).text(message).build();

        ChatPostMessageResponse response = methods.chatPostMessage(request);

        if (ssPath != null) {
          FilesUploadRequest filerequest =
              FilesUploadRequest.builder().channels(Arrays.asList(recipient)).file(new File(ssPath))
                  .filename("FailureSS").threadTs(response.getTs()).build();

          methods.filesUpload(filerequest);
        }
      }
    } catch (Exception e) {
      log.info(
          "Could not send failure details to slack channel due to: " + e.getLocalizedMessage());
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
