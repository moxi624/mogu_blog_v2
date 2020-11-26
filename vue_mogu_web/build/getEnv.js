const fs = require('fs');
const paths = require('path');

const appDirectory = fs.realpathSync(process.cwd());
const resolveApp = (relativePath) => paths.resolve(appDirectory, relativePath);
function parse(src, options) {
  const debug = Boolean(options && options.debug);
  const obj = {};

  src.toString().split('\n').forEach((line, idx) => {
    const keyValueArr = line.match(/^\s*([\w.-]+)\s*=\s*(.*)?\s*$/);
    if (keyValueArr != null) {
      const key = keyValueArr[1];

      let value = keyValueArr[2] || '';

      const len = value ? value.length : 0;
      if (len > 0 && value.charAt(0) === '"' && value.charAt(len - 1) === '"') {
        value = value.replace(/\\n/gm, '\n');
      }

      value = value.replace(/(^['"]|['"]$)/g, '').trim();

      obj[key] = value;
    } else if (debug) {
      // eslint-disable-next-line no-console
      console.log(`did not match key and value when parsing line ${idx + 1}: ${line}`);
    }
  });

  return obj;
}

function config(filePath) {
  const encoding = 'utf8';
  const debug = false;
  if (filePath) {
    try {
      const parsed = parse(fs.readFileSync(filePath, { encoding }), { debug });
      return { parsed };
    } catch (e) {
      return { error: e };
    }
  }
  throw new Error('.env path cannot be empty');
}

/**
 * 获取.env中的配置，解析为js字符串
 * @param {*} isDev
 */
function getEnv() {
  const customConfig = config(resolveApp('.env'));
  const combineEnv = customConfig.parsed;
  return `window._env_ = ${JSON.stringify(combineEnv)};`;
}

module.exports = getEnv;
