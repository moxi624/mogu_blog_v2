package com.moxi.mogublog.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.javassist.ClassClassPath;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.Modifier;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;



public class JoinPointInfoUtil {
	
	
	/**
	 * 获取切入点方法信息
	 * @param joinPoint
	 * @return joinPointInfo
	 * key : classPath 切入点方法类路径
	 * key : method 切入点方法名  
	 * key : paramMap 方法参数
	 * @throws ClassNotFoundException 
	 * @throws NotFoundException 
	 */
	public static Map<String,Object> getJoinPointInfoMap(JoinPoint joinPoint){
		Map<String,Object> joinPointInfo = new HashMap<>();
		
		String classPath = joinPoint.getTarget().getClass().getName();
		String method = joinPoint.getSignature().getName();
		joinPointInfo.put("classPath", classPath);
		joinPointInfo.put("method",method);
		
		Class<?> clazz = null;
		CtMethod ctMethod = null;
		LocalVariableAttribute attr = null;
		int length = 0;
		int pos = 0;
		try {
			clazz = Class.forName(classPath);
			String clazzName = clazz.getName();
			ClassPool pool = ClassPool.getDefault();
			ClassClassPath classClassPath = new ClassClassPath(clazz);
			pool.insertClassPath(classClassPath);
			org.apache.ibatis.javassist.CtClass ctClass = pool.get(clazzName);
			ctMethod = ctClass.getDeclaredMethod(method);
			MethodInfo methodInfo = ctMethod.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
			if(attr == null ) {
				return joinPointInfo;
			}
			length = ctMethod.getParameterTypes().length;
			pos = Modifier.isStatic(ctMethod.getModifiers())? 0 : 1;
		} catch (ClassNotFoundException e) {
			e.getStackTrace();
		} catch (NotFoundException e) {
			e.getStackTrace();
		}
		Map<String,Object> paramMap = new HashMap<>();
		Object[] paramsArgsValues = joinPoint.getArgs();
		String[] paramsArgsNames =  new String[length];
		for(int i = 0; i<length; i++) {
			paramsArgsNames[i] = attr.variableName(i+pos);
			String paramsArgsName = attr.variableName(i+pos);
			if(paramsArgsName.equalsIgnoreCase("request")||
					paramsArgsName.equalsIgnoreCase("response")||
					paramsArgsName.equalsIgnoreCase("session")) {
				break;
			}
			Object paramsArgsValue = paramsArgsValues[i];
			paramMap.put(paramsArgsName, paramsArgsValue);
			}
		joinPointInfo.put("paramMap", JSON.toJSONBytes(paramMap,
				SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteMapNullValue));
		return joinPointInfo;
	}
	
}
