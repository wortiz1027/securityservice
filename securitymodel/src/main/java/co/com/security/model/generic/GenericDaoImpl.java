package co.com.security.model.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class GenericDaoImpl<T, ID extends java.io.Serializable> implements GenericDao<T, ID> {

    private Class<T> clazz;
    @Autowired
    private SessionFactory sessionFactory;

    public GenericDaoImpl(Class<T> persistenceClass) {
        this.clazz = persistenceClass;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAllRows() {
        StringBuilder sQuery = new StringBuilder();

        sQuery.append("SELECT t \n");
        sQuery.append("FROM ");
        sQuery.append(getClazz().getSimpleName());
        sQuery.append(" t");

        Query<T> query = getSessionFactory().getCurrentSession().createQuery(sQuery.toString(), clazz);

        //List<T> entities = castGenerics(getClazz(), query.getResultList());

        return castGenerics(getClazz(), query.getResultList());
    }

    @Override
    @Transactional(readOnly = true)
    public T getById(ID id) {

        Object object = getSessionFactory()
                .getCurrentSession()
                .get(getClazz(), id);

        if (getClazz().isInstance(object)){
            //T entity = getClazz().cast(object);
            return getClazz().cast(object);
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getByDescription(String field, String ipParam, String desc) {

        Criteria criteria = ((Criteria) getSessionFactory()
                .getCurrentSession()
                .getCriteriaBuilder()
                .createQuery(clazz))
                .add(Restrictions.like(ipParam, desc, MatchMode.ANYWHERE));


        //List<T> entities = castGenerics(getClazz(), criteria.list());

        return castGenerics(getClazz(), criteria.list());
    }

    @Override
    @Transactional
    public T save(T entity) {
        if (entity != null){
            getSessionFactory()
                    .getCurrentSession()
                    .persist(entity);
            return entity;
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(ID id) {

        try{
            Object object = getSessionFactory().getCurrentSession().get(getClazz(), id);

            if (getClazz().isInstance(object)){
                T entity = getClazz().cast(object);
                getSessionFactory().getCurrentSession().delete(entity);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public T update(T entity) {
        if (entity != null){
            getSessionFactory()
                    .getCurrentSession().update(entity);;
            return entity;
        }
        return null;
    }

    public static <T> List<T> castGenerics(Class<? extends T> clazz, Collection<?> collections) {
        List<T> list = new ArrayList<T>(collections.size());

        for(Object object : collections)
            list.add(clazz.cast(object));

        return list;
    }

    @Override
    public void flush() {

    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}