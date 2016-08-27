package org.hadoopinternal.ipc;

public class IPCQueryStatusImpl implements IPCQueryStatus {
	public IPCQueryStatusImpl() {
	}

	@Override
	public IPCFileStatus getFileStatus(String filename) {
		IPCFileStatus status = new IPCFileStatus(filename);
		System.out.println("Method getFileStatus Called, return: " + status);
		return status;
	}

}