package run.app.translator.utils;

import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.util.Map;

public class ReflectUtil {
    public static Object getTarget(Object dest, Map<String, Object> addProperties) {
        PropertyUtilsBean propertyUtilsBean =new PropertyUtilsBean();
        PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(dest);
        Map<String, Class> propertyMap = Maps.newHashMap();
        for(PropertyDescriptor d : descriptors) {
            if(!"class".equalsIgnoreCase(d.getName())) {
                propertyMap.put(d.getName(), d.getPropertyType());
            }
        }
        // add extra properties
        addProperties.forEach((k, v) -> propertyMap.put(k, v.getClass()));
        // new dynamic bean
        DynamicBean dynamicBean =new DynamicBean(dest.getClass(), propertyMap);
        // add old value
        propertyMap.forEach((k, v) -> {
            try{
                // filter extra properties
                if(!addProperties.containsKey(k)) {
                    dynamicBean.setValue(k, propertyUtilsBean.getNestedProperty(dest, k));
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        // add extra value
        addProperties.forEach((k, v) -> {
            try{
                dynamicBean.setValue(k, v);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        return dynamicBean.getTarget();
    }

    public static class DynamicBean {
        private Object target;

        private BeanMap beanMap;

        public DynamicBean(Class superclass, Map<String, Class> propertyMap) {
            this.target = generateBean(superclass, propertyMap);
            this.beanMap = BeanMap.create(this.target);
        }

        public void setValue(String property, Object value) {
            beanMap.put(property, value);
        }

        public Object getValue(String property) {
            return beanMap.get(property);
        }

        public Object getTarget() {
            return this.target;
        }

        private Object generateBean(Class superclass, Map<String, Class> propertyMap) {
            BeanGenerator generator =new BeanGenerator();
            if(null != superclass) {
                generator.setSuperclass(superclass);
            }
            BeanGenerator.addProperties(generator, propertyMap);
            return generator.create();
        }
    }
}
