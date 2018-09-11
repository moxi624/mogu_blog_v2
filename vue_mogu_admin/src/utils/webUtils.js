/**
 * Created by jiachenpan on 16/11/18.
 */

export function formatData(arr) {
  const params = new URLSearchParams()
  for (var key in arr) {
    params.append(key, arr[key])
  }
  return params
}

