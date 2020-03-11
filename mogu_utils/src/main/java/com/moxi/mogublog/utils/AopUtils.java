package com.moxi.mogublog.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.*;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 切面相关工具类
 *
 * @author: 陌溪
 * @create: 2020-01-21-12:34
 */
@Slf4j
public class AopUtils {

    /**
     * 获取AOP代理的方法的参数名称和参数值
     *
     * @param cls
     * @param clazzName
     * @param methodName
     * @param args
     * @return
     * @throws NotFoundException
     */
    public static StringBuffer getNameAndArgs(Class<?> cls, String clazzName, String methodName, Object[] args) {

        StringBuffer sb = new StringBuffer();

        try {
            Map<String, Object> nameAndArgs = new HashMap<>();

            ClassPool pool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(cls);
            pool.insertClassPath(classPath);

            CtClass cc = pool.get(clazzName);
            CtMethod cm = cc.getDeclaredMethod(methodName);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                // exception
            }
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < cm.getParameterTypes().length; i++) {
                // paramNames即参数名
                nameAndArgs.put(attr.variableName(i + pos), args[i]);
            }

            // nameAndArgs的两种类型，用实体类接收的类似这样：
            // reqParams=com.whoareyou.fido.rest.User@616b9c0e
            // 用Map<String,Object>接收的是这样：menuNo=56473283，遍历这个map区分两种不同，使用不同的取值方式。
            // 根据获取到的值所属的不同类型通过两种不同的方法获取参数
            boolean flag = false;
            if (nameAndArgs != null && nameAndArgs.size() > 0) {
                for (Map.Entry<String, Object> entry : nameAndArgs.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        flag = true;
                        break;
                    }
                }
            }
            sb = new StringBuffer();
            if (flag) {
                // 从Map中获取
                sb.append(JSON.toJSONString(nameAndArgs));
            } else {
                if (args != null) {
                    for (Object object : args) {
                        if (object != null) {
                            if (object instanceof MultipartFile || object instanceof ServletRequest
                                    || object instanceof ServletResponse) {
                                continue;
                            }
                            sb.append(JSON.toJSONString(object));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return sb;
    }

    /**
     * 获取AOP代理的方法的参数名称和参数值
     *
     * @param cls
     * @param clazzName
     * @param methodName
     * @param args
     * @return
     */
    public static Map<String, Object> getNameAndArgsMap(Class<?> cls, String clazzName, String methodName, Object[] args) {

        Map<String, Object> nameAndArgs = new HashMap<>();

        try {

            ClassPool pool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(cls);
            pool.insertClassPath(classPath);

            CtClass cc = pool.get(clazzName);
            CtMethod cm = cc.getDeclaredMethod(methodName);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                // exception
            }
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < cm.getParameterTypes().length; i++) {
                String variableName = attr.variableName(i + pos);
                System.out.println("参数名" + attr.variableName(i + pos));
                // paramNames即参数名
                nameAndArgs.put(attr.variableName(i + pos), args[i]);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return nameAndArgs;
    }

    /**
     * 获取参数名和值
     *
     * @param joinPoint
     * @return
     */
    public static Map getFieldsName(ProceedingJoinPoint joinPoint) throws ClassNotFoundException, NoSuchMethodException {
        // 参数值
        Object[] args = joinPoint.getArgs();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();

        // 通过map封装参数和参数值
        HashMap<String, Object> paramMap = new HashMap();
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }
        return paramMap;
    }
}
