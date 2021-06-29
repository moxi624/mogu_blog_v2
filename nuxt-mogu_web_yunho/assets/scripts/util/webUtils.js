import web from '../config/webconst'
import {
  validatenull
} from './validate'
/**
 * WebUtil常用的一些工具类
 */

 export function formatData(arr) {
    const params = new URLSearchParams()
    for (var key in arr) {
      params.append(key, arr[key])
    }
    return params
  }
  
  /**
   * 将日期时间转换为指定格式，如：YYYY-mm-dd HH:MM表示2019-06-06 19:45
   * 例如：dateFormat("YYYY-mm-dd HH:MM", date)
   * @param fmt
   * @param date
   * @returns {*}
   */
  export function dateFormat(fmt, date) {
    let ret;
    const opt = {
      "Y+": date.getFullYear().toString(),        // 年
      "m+": (date.getMonth() + 1).toString(),     // 月
      "d+": date.getDate().toString(),            // 日
      "H+": date.getHours().toString(),           // 时
      "M+": date.getMinutes().toString(),         // 分
      "S+": date.getSeconds().toString()          // 秒
      // 有其他格式化字符需求可以继续添加，必须转化成字符串
    };
    for (let k in opt) {
      ret = new RegExp("(" + k + ")").exec(fmt);
      if (ret) {
        fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
      };
    };
    return fmt;
  }
  
  /**
   * dateTimeStamp是一个时间毫秒，注意时间戳是秒的形式，在这个毫秒的基础上除以1000，就是十位数的时间戳。13位数的都是时间毫秒。
   * @param dateTimeStamp
   * @returns {string}
   */
  export function timeAgo(dateTimeStamp) {
    let result = "";
    let minute = 1000 * 60;      //把分，时，天，周，半个月，一个月用毫秒表示
    let hour = minute * 60;
    let day = hour * 24;
    let week = day * 7;
    let month = day * 30;
    let year = day * 365;
    let now = new Date().getTime();   //获取当前时间毫秒
    dateTimeStamp = dateTimeStamp.substring(0, 18);
    //必须把日期'-'转为'/'
    dateTimeStamp = dateTimeStamp.replace(/-/g, '/');
  
    let timestamp = new Date(dateTimeStamp).getTime();
  
    let diffValue = now - timestamp;//时间差
  
    if (diffValue < 0) {
      return result;
    }
    let minC = diffValue / minute;  //计算时间差的分，时，天，周，月
    let hourC = diffValue / hour;
    let dayC = diffValue / day;
    let weekC = diffValue / week;
    let monthC = diffValue / month;
    let yearC = diffValue / year;
  
    minC = parseInt(minC)
    hourC = parseInt(hourC)
    dayC = parseInt(dayC)
    weekC = parseInt(weekC)
    monthC = parseInt(monthC)
    yearC = parseInt(yearC)
  
    if (monthC > 12) {
      result = " " + parseInt(yearC) + "年前"
    } else if (monthC >= 1 && monthC < 12) {
      result = " " + parseInt(monthC) + "月前"
    } else if (weekC >= 1 && weekC <= 4) {
      result = " " + parseInt(weekC) + "周前"
    } else if (dayC >= 1 && dayC <= 6) {
      result = " " + parseInt(dayC) + "天前"
    } else if (hourC >= 1 && hourC <= 23) {
      result = " " + parseInt(hourC) + "小时前"
    } else if (minC >= 1 && minC <= 59) {
      result = " " + parseInt(minC) + "分钟前"
    } else if (diffValue >= 0 && diffValue <= minute) {
      result = "刚刚"
    }
    return result;
  }
  const keyName = web.key + '-';
  /**web
 * 存储localStorage
 */
export const setStore = (params = {}) => {
  let {
    name,
    content,
    type,
  } = params;
  name = keyName + name
  let obj = {
    dataType: typeof (content),
    content: content,
    type: type,
    datetime: new Date().getTime()
  }
  if (type) window.sessionStorage.setItem(name, JSON.stringify(obj));
  else window.localStorage.setItem(name, JSON.stringify(obj));
}
/**
 * 获取localStorage
 */

export const getStore = (params = {}) => {
  let {
    name,
    debug
  } = params;
  name = keyName + name
  let obj = {},
    content;
  obj = window.sessionStorage.getItem(name);
  if (validatenull(obj)) obj = window.localStorage.getItem(name);
  if (validatenull(obj)) return;
  try {
    obj = JSON.parse(obj);
  } catch{
    return obj;
  }
  if (debug) {
    return obj;
  }
  if (obj.dataType == 'string') {
    content = obj.content;
  } else if (obj.dataType == 'number') {
    content = Number(obj.content);
  } else if (obj.dataType == 'boolean') {
    content = eval(obj.content);
  } else if (obj.dataType == 'object') {
    content = obj.content;
  }
  return content;
}
/**
 * 删除localStorage
 */
export const removeStore = (params = {}) => {
  let {
    name,
    type
  } = params;
  name = keyName + name
  if (type) {
    window.sessionStorage.removeItem(name);
  } else {
    window.localStorage.removeItem(name);
  }

}

/**
 * 获取全部localStorage
 */
export const getAllStore = (params = {}) => {
  let list = [];
  let {
    type
  } = params;
  if (type) {
    for (let i = 0; i <= window.sessionStorage.length; i++) {
      list.push({
        name: window.sessionStorage.key(i),
        content: getStore({
          name: window.sessionStorage.key(i),
          type: 'session'
        })
      })
    }
  } else {
    for (let i = 0; i <= window.localStorage.length; i++) {
      list.push({
        name: window.localStorage.key(i),
        content: getStore({
          name: window.localStorage.key(i),
        })
      })

    }
  }
  return list;

}

/**
 * 清空全部localStorage
 */
export const clearStore = (params = {}) => {
  let { type } = params;
  if (type) {
    window.sessionStorage.clear();
  } else {
    window.localStorage.clear()
  }

}

  
  