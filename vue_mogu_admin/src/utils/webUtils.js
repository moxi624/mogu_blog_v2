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

export function formatDataToJson(arr) {
  var params = "["  
  for (var key in arr) {
    params = params + key + ":" + arr[key] + ","
  }
  params += "]"
  return params
}

export function formatDataToForm(arr) {
  const params = new FormData() 
  for (var key in arr) {
    console.log('"' + key + '"', arr[key])
    params.append('"' + key + '"', arr[key])
  }
  return params
}