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
import org.ros.concurrent.CancellableLoop;
import org.ros.exception.RemoteException;
import org.ros.exception.ServiceNotFoundException;
import org.ros.message.MessageFactory;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.service.ServiceClient;
import org.ros.node.service.ServiceResponseListener;
import org.ros.node.topic.Publisher;

import rosjava_standalone_msgs.MyMessage;
import rosjava_standalone_msgs.MyService;
import rosjava_standalone_msgs.MyServiceRequest;
import rosjava_standalone_msgs.MyServiceResponse;

/**
 * A simple {@link Publisher} {@link NodeMain}.
 */
public class Talker extends AbstractNodeMain {

	private ServiceClient<MyServiceRequest, MyServiceResponse> serviceClient;

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("rosjava/talker");
	}

	@Override
	public void onStart(final ConnectedNode connectedNode) {
		final Log log = connectedNode.getLog();

		final Publisher<std_msgs.String> publisher = connectedNode.newPublisher("chatter", std_msgs.String._TYPE);

		final Publisher<MyMessage> publisher2 = connectedNode.newPublisher("my_message", MyMessage._TYPE);

		final MessageFactory messageFactory = connectedNode.getTopicMessageFactory();
		
		// This CancellableLoop will be canceled automatically when the node shuts
		// down.
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			private int sequenceNumber;

			@Override
			protected void setup() {
				sequenceNumber = 0;
			}

			@Override
			protected void loop() throws InterruptedException {
				std_msgs.String str = publisher.newMessage();
				str.setData("Hello world! " + sequenceNumber);
				publisher.publish(str);

				// publish custom message
				MyMessage mymessage = publisher2.newMessage();
				mymessage.getHeader().setSeq(sequenceNumber);
				publisher2.publish(mymessage);

				// call custom service
				if (serviceClient == null) {
					try {
						serviceClient = connectedNode.newServiceClient("my_service", MyService._TYPE);
					} catch (ServiceNotFoundException e) {
						// throw new RosRuntimeException(e);
						log.info("service not found");
					}
				}
				if (serviceClient != null) {
					// service found
					MyServiceRequest request = serviceClient.newMessage();
					MyMessage message = messageFactory.newFromType(MyMessage._TYPE);
					mymessage.getHeader().setSeq(sequenceNumber);
					request.getMessages().add(message);
					serviceClient.call(request, new ServiceResponseListener<MyServiceResponse>() {
						@Override
						public void onSuccess(MyServiceResponse response) {
							log.info("service response: " + response.getMessages().get(0).getHeader().getSeq());
						}
						
						@Override
						public void onFailure(RemoteException e) {
							log.error("service error: " + e.toString());
							serviceClient = null;
						}
					});
				}

				sequenceNumber++;
				Thread.sleep(1000);
			}
		});
	}

}
