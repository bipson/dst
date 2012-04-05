package dst1.db;

import dst1.model.Membership;
import dst1.model.MembershipPK;

public class MembershipDao extends GenericDao<Membership, MembershipPK> {
	
	public MembershipDao() {
		super(Membership.class);
	}
	
}
