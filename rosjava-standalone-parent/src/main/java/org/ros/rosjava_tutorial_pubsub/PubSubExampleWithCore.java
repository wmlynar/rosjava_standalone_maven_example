package org.ros.rosjava_tutorial_pubsub;

import org.ros.RosCore;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class PubSubExampleWithCore {
	private static String[] EMPTY = { "" };
	// Create instances for Talker and Listener
	private static Talker pubNodeMain = new Talker();
	private static Listener subNodeMain = new Listener();

	public static void main(String[] args) {

		// start roscore
		RosCore rosCore = RosCore.newPublic(11311);
		rosCore.start();
		try {
			rosCore.awaitStart();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Set up the executor for both of the nodes
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();

		NodeConfiguration nodeConfiguration = null;
		if (args.length == 0) {
			args = EMPTY;
		}
		CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(args));
		nodeConfiguration = loader.build();

		// Check if Talker class correctly instantiated
		Preconditions.checkState(pubNodeMain != null);
		// execute the nodelet talker (this will run the method onStart of Talker.java)
		nodeMainExecutor.execute(pubNodeMain, nodeConfiguration);

		// Load the subscriber(listener) same as for publisher
		Preconditions.checkState(subNodeMain != null);
		nodeMainExecutor.execute(subNodeMain, nodeConfiguration);

	}

}
