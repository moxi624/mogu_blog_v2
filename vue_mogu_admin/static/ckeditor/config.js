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

	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.removeButtons = 'Underline,Subscript,Superscript';

	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

	//设置语言
	config.language = 'zh-cn';

	// Simplify the dialog windows.
	config.removeDialogTabs = 'image:advanced;link:advanced';

	//开启工具栏“图像”中文件上传功能，后面的url为待会要上传action要指向的的action或servlet
	config.filebrowserImageUploadUrl = "http://localhost:8602/ckeditor/imgUpload";

	//开启插入\编辑超链接中文件上传功能，后面的url为待会要上传action要指向的的action或servlet                                                                                                   
	config.filebrowserUploadUrl = 'http://localhost:8602/ckeditor/fileUpload';

	//开启flash中文件上传，这里因为不常用，我暂时就不配置了
	//config.filebrowserFlashUploadUrl="";

	//工具栏“图像”中预览区域显示内容 
	config.image_previewText = ' ';
};
