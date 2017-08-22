package org.ros.rosjava_tutorial_services;

import java.net.URI;

import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import com.google.common.base.Preconditions;

public class SlientServerExampleWithoutCore {
	// Create instances for Talker and Listener
	private static Client clientNode = new Client();
	private static Server serverNode = new Server();

	public static void main(String[] args) {
		// Set up the executor for both of the nodes
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		
		// This is because I work on a remote machine masteruri has the roscore machine
		// IP and host is the local IP
		URI masteruri = URI.create("http://127.0.0.1:11311");
		String host = "127.0.0.1";

		// Load the publisher(talker)
		NodeConfiguration serverNodeConfiguration = NodeConfiguration.newPublic(host, masteruri);
		// Check if Talker class correctly instantiated
		Preconditions.checkState(serverNode != null);
		// execute the nodelet talker (this will run the method onStart of Talker.java)
		nodeMainExecutor.execute(serverNode, serverNodeConfiguration);

		// Load the subscriber(listener) same as for publisher
		NodeConfiguration clientNodeConfiguration = NodeConfiguration.newPublic(host, masteruri);
		Preconditions.checkState(clientNode != null);
		nodeMainExecutor.execute(clientNode, clientNodeConfiguration);

	}

}
