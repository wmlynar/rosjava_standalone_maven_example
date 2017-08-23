/*
 * Copyright (C) 2014 woj.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.wmlynar.rosjava_standalone.generated_messages;

import org.apache.commons.logging.Log;
import org.ros.exception.ServiceException;
import org.ros.message.MessageFactory;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.service.ServiceResponseBuilder;
import org.ros.node.topic.Subscriber;

import rosjava_standalone_msgs.MyMessage;
import rosjava_standalone_msgs.MyService;
import rosjava_standalone_msgs.MyServiceRequest;
import rosjava_standalone_msgs.MyServiceResponse;

/**
 * A simple {@link Subscriber} {@link NodeMain}.
 */
public class Listener extends AbstractNodeMain {

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("rosjava/listener");
  }

  @Override
  public void onStart(ConnectedNode connectedNode) {
    final Log log = connectedNode.getLog();
    Subscriber<std_msgs.String> subscriber = connectedNode.newSubscriber("chatter", std_msgs.String._TYPE);
    subscriber.addMessageListener(new MessageListener<std_msgs.String>() {
      @Override
      public void onNewMessage(std_msgs.String message) {
        log.info("I heard: \"" + message.getData() + "\"");
      }
    });
    
    // add custom topic listener
    Subscriber<MyMessage> subscriber2 = connectedNode.newSubscriber("my_message", MyMessage._TYPE);
    subscriber2.addMessageListener(new MessageListener<MyMessage>() {
		@Override
		public void onNewMessage(MyMessage message) {
			log.info("custom message: " + message.getHeader().getSeq());
		}
	});
    
    // add custom service
	final MessageFactory messageFactory = connectedNode.getTopicMessageFactory();
    connectedNode.newServiceServer("my_service", MyService._TYPE, new ServiceResponseBuilder<MyServiceRequest, MyServiceResponse>() {
		@Override
		public void build(MyServiceRequest request, MyServiceResponse response) throws ServiceException {
			log.info("service call: " + request.getMessages().get(0).getHeader().getSeq());
			MyMessage message = messageFactory.newFromType(MyMessage._TYPE);
			message.getHeader().setSeq(request.getMessages().get(0).getHeader().getSeq() + 1);
			response.getMessages().add(message);
		}

	});
  }
}
