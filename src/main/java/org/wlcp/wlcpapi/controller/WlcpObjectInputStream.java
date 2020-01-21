package org.wlcp.wlcpapi.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class WlcpObjectInputStream extends ObjectInputStream {
	
	public WlcpObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();

        if (resultClassDescriptor.getName().equals("wlcp.model.master.Game"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.Game.class);
        if (resultClassDescriptor.getName().equals("wlcp.model.master.Username"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.Username.class);
        
        if (resultClassDescriptor.getName().equals("wlcp.model.master.state.State"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.state.State.class);
        if (resultClassDescriptor.getName().equals("wlcp.model.master.state.StartState"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.state.StartState.class);
        if (resultClassDescriptor.getName().equals("wlcp.model.master.state.OutputState"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.state.OutputState.class);
        if (resultClassDescriptor.getName().equals("wlcp.model.master.state.PictureOutput"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.state.PictureOutput.class);
        if (resultClassDescriptor.getName().equals("wlcp.model.master.state.StateType"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.state.StateType.class);
        
        if (resultClassDescriptor.getName().equals("wlcp.model.master.connection.Connection"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.connection.Connection.class);
        
        if (resultClassDescriptor.getName().equals("wlcp.model.master.transition.Transition"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.transition.Transition.class);
        if (resultClassDescriptor.getName().equals("wlcp.model.master.transition.SingleButtonPress"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.transition.SingleButtonPress.class);
        if (resultClassDescriptor.getName().equals("wlcp.model.master.transition.SequenceButtonPress"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.transition.SequenceButtonPress.class);
        if (resultClassDescriptor.getName().equals("wlcp.model.master.transition.KeyboardInput"))
            resultClassDescriptor = ObjectStreamClass.lookup(org.wlcp.wlcpapi.datamodel.master.transition.KeyboardInput.class);

        return resultClassDescriptor;
    }
}
