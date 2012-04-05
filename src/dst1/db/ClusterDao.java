package dst1.db;

import dst1.model.Cluster;

public class ClusterDao extends GenericDao<Cluster, Long> {
	
	public ClusterDao() {
		super(Cluster.class);
	}
	
}
