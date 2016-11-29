package common.rank;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.net.DNSToSwitchMappingWithDependency;

//implements DNSToSwitchMappingWithDependency
public class MyDNSToSwitchMapping implements DNSToSwitchMappingWithDependency{

	public List<String> resolve(List<String> names) {
		List<String> paths = new ArrayList<String>();
		if (names != null && !names.isEmpty()) {
			System.out.println(names.size() + " " + names.get(0));
			for (String hostname : paths) {
				// Integer no = Integer.parseInt(hostname.substring(1));
				String rackPaht = "";
				if (hostname == "sdf") {

				}
			}
		}
		return paths;
	}

	@Override
	public void reloadCachedMappings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reloadCachedMappings(List<String> names) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getDependency(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
