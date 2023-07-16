package com.badbones69.crazycrates.core.frame.command;

public interface CloudCommandFlow {

    void addCommand(CloudCommandEngine engine);

    //TODO() I don't think this will work with cloud, the way the command tree in cloud is made. I can delete all commands but individual ones seems iffy.
    //void removeCommand(CloudCommandEngine engine);

}