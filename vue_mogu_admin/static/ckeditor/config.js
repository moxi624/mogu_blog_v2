/**
 * @license Copyright (c) 2003-2018, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function (config) {
	// Define changes to default configuration here.
	// For complete reference see:
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config

	// The toolbar groups arrangement, optimized for two toolbar rows.
	config.toolbarGroups = [
		{ name: 'clipboard', groups: ['clipboard', 'undo'] },
		{ name: 'editing', groups: ['find', 'selection', 'spellchecker'] },
		{ name: 'links' },
		{ name: 'insert' },
		{ name: 'forms' },
		{ name: 'tools' },
		{ name: 'document', groups: ['mode', 'document', 'doctools'] },
		{ name: 'others' },
		'/',
		{ name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
		{ name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi'] },
		{ name: 'styles' },
		{ name: 'colors' },
		{ name: 'about' }
	];

	//删除标准插件提供的一些按钮
	//标准工具栏中不需要。
	config.removeButtons = 'Underline,Subscript,Superscript';

	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

	//添加插件，多个插件用逗号隔开
	// codesnippet: 代码块   ， uploadimage: 上传图片,  colorbutton: 颜色按钮
	config.extraPlugins = 'codesnippet,colorbutton';
	
	//配置代码块风格
	codeSnippet_theme: 'zenburn';

    //使用zenburn的代码高亮风格样式 PS:zenburn效果就是黑色背景
    //如果不设置着默认风格为default
    
	//设置语言
	config.language = 'zh-cn';

	// 简化对话框窗口。
	config.removeDialogTabs = 'image:advanced;link:advanced';

	//开启工具栏“图像”中文件上传功能，后面的url为待会要上传action要指向的的action或servlet
	config.filebrowserImageUploadUrl = "http://localhost:8602/ckeditor/imgUpload?";

	//开启插入\编辑超链接中文件上传功能，后面的url为待会要上传action要指向的的action或servlet                                                                                                   
	config.filebrowserUploadUrl = 'http://localhost:8602/ckeditor/fileUpload';

	//开启flash中文件上传，这里因为不常用，我暂时就不配置了
	//config.filebrowserFlashUploadUrl="";

	//工具栏“图像”中预览区域显示内容 
	config.image_previewText = ' ';

	config.font_names = '宋体/宋体;黑体/黑体;仿宋/仿宋_GB2312;楷体/楷体_GB2312;隶书/隶书;幼圆/幼圆;雅黑/雅黑;'+ config.font_names ;
};
