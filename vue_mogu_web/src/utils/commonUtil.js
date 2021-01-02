import { Message } from 'element-ui'
// import {showdown} from 'showdown'
// import {TurndownService} from 'turndown'

/** **********************************************************/
/**
 *  全局状态码
 */
const ECode = {
  // 默认页大小
  SUCCESS: "success",
  // 默认页码
  ERROR: "error",
}

/**
 * 全局配置文件
 */
const SysConf = {
  defaultAvatar: "https://gitee.com/moxi159753/wx_picture/raw/master/picture/favicon.png", // 默认头像
}

/** **********************************************************/

/**
 * 通用工具类
 */
const FUNCTIONS = {

  /**
   * 标签转字符串
   * @param tags
   * @returns {string}
   */
  tagsToString: tags => {
    let str = ''
    for (let i = 0; i < tags.length; i++) {
      str += tags[i] + ','
    }
    return str.substr(0, str.length - 1)
  },
  /**
   * 字符串转标签
   * @param str
   * @returns {Array}
   */
  stringToTags: str => {
    if (str !== null && str !== '') {
      return str.split(',')
    } else {
      return []
    }
  },
  // 切割字符串
  splitStr: (str, flagCount) => {
    if (str == null || str == '') {
      return ""
    } else if(str.length > flagCount) {
      return str.substring(0, flagCount) + "..."
    } else {
      return str
    }
  },
  /**
   * 复制文字到剪切板
   * @param text
   */
  copyText: text => {
    const oInput = document.createElement('input')
    oInput.value = text
    document.body.appendChild(oInput)
    oInput.select() // 选择对象
    document.execCommand('Copy') // 执行浏览器复制命令
    oInput.className = 'oInput'
    oInput.style.display = 'none'
  },
  /**
   * 将Markdown转成Html
   * @param text
   */
  markdownToHtml: text => {
    let converter = new showdown.Converter();
    return converter.makeHtml(text);
  },
  /**
   * 将Html转成Markdown
   * @param text
   */
  htmlToMarkdown: text => {
    var turndownService = new TurndownService()
    return turndownService.turndown(text)
  },
  /**
   * 将Html转成Markdown文件
   * @param title：标题
   * @param text：正文
   */
  htmlToMarkdownFile: (title, text) => {

    title = title || "默认标题"

    let turndownService = new TurndownService()

    let markdown = turndownService.turndown(text)

    //创建一个blob对象,file的一种
    let blob = new Blob([markdown])

    let link = document.createElement('a')

    link.href = window.URL.createObjectURL(blob)

    //配置下载的文件名
    link.download = title + '.md'

    link.click()
  },
  /**
   * 通用提示信息
   * @type {{success: message.success, warning: message.warning, error: message.error, info: message.info}}
   */
  message: {
    success: function(message) {
      Message({
        showClose: true,
        message: message || '成功',
        type: 'success'
      })
    },
    warning: function(message) {
      Message({
        showClose: true,
        message: message || '警告',
        type: 'warning'
      })
    },
    info: function(message) {
      Message({
        showClose: true,
        message: message || '提示'
      })
    },
    error: function(message) {
      Message({
        showClose: true,
        message: message || '异常',
        type: 'error'
      })
    }
  }
}

export default {
  ECode,
  SysConf,
  FUNCTIONS
}
