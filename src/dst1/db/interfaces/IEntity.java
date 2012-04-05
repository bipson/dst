package dst1.db.interfaces;

import java.io.Serializable;

public interface IEntity<EntityKeyType extends Serializable> extends Serializable {
	
    public EntityKeyType obtainKey();
    
}
