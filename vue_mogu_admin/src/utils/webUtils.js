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
