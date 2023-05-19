package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.packet.Packet;

public interface IPeer {
    Packet getNextPacket();
    boolean hasNextPacket();
}
