package preevisiontosimulink.proxy.port;

import preevisiontosimulink.proxy.block.ISimulinkBlock;

public interface ISimulinkPort {
    String getName();
	ISimulinkBlock getParent();
}