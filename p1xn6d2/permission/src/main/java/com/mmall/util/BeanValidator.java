package com.mmall.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.exception.ParamException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;


/**
 * @ClassName : BeanValidator Created with IDEA
 *
 * @author:CarlLing @CreateDate : 2019-01-12 0:43 @Description : ����У�鹤����
 */
public class BeanValidator {

  protected static final Logger log = LoggerFactory.getLogger(BeanValidator.class);

  //����һ���Լ��Ĺ���
  private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

  /**
   *
   * @param t K �����ֶ�   V��������Ϣ  object to validate
   * @param groups
   * @param <T>
   * @return
   */
  public static <T>Map<String, String> validate(T t, Class... groups) {
    //�ӹ������ȡ
    Validator validator = validatorFactory.getValidator();
    //��ȡУ����
    Set validatorResult = validator.validate(t, groups);
    //��Ϊ�գ���У�����
    if (validatorResult.isEmpty()) {
        log.info("collections.emptyMap=====" + Collections.emptyMap());
        return Collections.emptyMap();
    } else {
        LinkedHashMap errors = Maps.newLinkedHashMap();
        Iterator iterator = validatorResult.iterator();
        while (iterator.hasNext()) {
          //����һ��violation��Լ�����˶���¶�� violation��������Լ����violation����
          ConstraintViolation violation = (ConstraintViolation) iterator.next();
          errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
      return errors;
    }
  }

  /**
   *  listУ��
   * @param collection
   * @return
   */
  public static Map<String, String> validateList(Collection<?> collection) {
     /*
         Preconditions��guava�ṩ�����ڽ��д���У��Ĺ�����,���������ǹ����򿪷��жԴ����У���Ԥ ����
         checkArgument(boolean expression)������У����ʽ�Ƿ�Ϊ�棬һ������������У�����
         checkArgument(boolean expression, @Nullable Object errorMessage)��У����ʽ�Ƿ�Ϊ�棬��Ϊ��ʱ��ʾָ���Ĵ�����Ϣ��
         checkArgument(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs)��
              У����ʽ�Ƿ�Ϊ�棬��Ϊ��ʱ��ʾ������Ϣ��������Ϣ������ʹ��ռλ����
         checkState(boolean expression)��У����ʽ�Ƿ�Ϊ�棬һ������У�鷽�������Ƿ�Ϊ�档
         checkState(boolean expression, @Nullable Object errorMessage)�������ʽΪ�ٵ�ʱ����ʾָ���Ĵ�����Ϣ��
         checkState(boolean expression,@Nullable String errorMessageTemplate,@Nullable Object... errorMessageArgs)�������ڴ�����Ϣ��ʹ��ռλ��
         checkNotNull(T reference)��У������Ƿ�Ϊ�ա�
         checkNotNull(T reference, @Nullable Object errorMessage)������Ϊ��ʱ��ʾָ���Ĵ�����Ϣ��
         checkNotNull(T reference, @Nullable String errorMessageTemplate,@Nullable Object... errorMessageArgs)�������ڴ�����Ϣ��ʹ��ռλ����
         checkPositionIndex(int index, int size, @Nullable String desc)��У��Ԫ�ص�����ֵ�Ƿ���Ч��index���ڵ���0С�ڵ���size������Чʱ��ʾ����������Ϣ��
         checkPositionIndex(int index, int size)������������ϢΪ��index��
         checkPositionIndexes(int start, int end, int size)��У����ڵ���start��С��end��list�ĳ����Ƿ�Ϊsize��
         */
     //google�ṩ�Ļ���У�鹤����
    Preconditions.checkNotNull(collection);
    Iterator iterator = collection.iterator();
    Map errors;
    do {
      if (!iterator.hasNext()) {
        return Collections.emptyMap();
      }
      Object object = iterator.next();
      errors = validate(object, new Class[0]);

    } while (errors.isEmpty());
    return errors;
  }

  /**
   * objectУ��
   * @param first
   * @param objects
   * @return
   */
  public static Map<String, String> validateObject(Object first, Object... objects) {
    if (objects != null && objects.length > 0) {
      return validateList(Lists.asList(first, objects));
    } else {
      return validate(first, new Class[0]);
    }
  }

  /**
   * ͨ���׳��쳣����ʽ���в���У�飬������ķ�����װ�������ӷ���
   * @param param  ����У�����
   * @throws ParamException ����У���쳣
   */
  public static void check(Object param) throws ParamException {
    log.info("check......");
    Map<String, String> map = BeanValidator.validateObject(param);
    log.info("map:" + map.toString());
    // if(map != null && map.entrySet().size() > 0 ){
    //maven commons-collections
    if (!MapUtils.isEmpty(map)) {
      throw new ParamException(map.toString());
    }
  }
}
