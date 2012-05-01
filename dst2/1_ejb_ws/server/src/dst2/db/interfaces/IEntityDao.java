package dst2.db.interfaces;

import java.io.Serializable;

public interface IEntityDao <EntityType extends IEntity<EntityKeyType>, EntityKeyType extends Serializable> {
	
	public void delete(EntityType entity);
	public void persist(EntityType entity);
	public void refresh(EntityType entity);
	public EntityType update(EntityType entity);
	public EntityType get(EntityKeyType keyType);
	
}