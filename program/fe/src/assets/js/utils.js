/*
 * utils.js
 * ver.1.1.20120323
 * 
 * Makoto Miyazaki (email: makoto.miyazaki.dc@hosei.ac.jp)
 * Hosei University
 * 
 */
 
/**
 * submitForm($formname, $bool)
 * @param $formname
 * @param $bool
 * @return boolean
 */
function submitForm($formname, $bool){
        document.forms[$formname].submit();
        return Boolean($bool);
}

/**
 * submitConfirm($message, $formname, $bool)
 * @param $message
 * @param $formname
 * @return boolean
 */
function submitConfirm($message, $formname){
		$bool = confirm($message);
		if ($bool){
			document.forms[$formname].submit();
		}
        return Boolean($bool);
}

/**
 * isDisabled($formname, $itemname)
 * @param $formname
 * @param $itemname
 * @return boolean
 */
function isDisabled($formname, $itemname){
        return document.forms[$formname].elements[$itemname].disabled;
}

/**
 * isEmpty($formname, $itemname)
 * @param $formname
 * @param $itemname
 * @return boolean
 */
function isEmpty($formname, $itemname){
        if (document.forms[$formname].elements[$itemname].value == ""){
                ret = true;
        }else{
                ret = false;
        }
        return ret;
}

/**
 * isChecked($formname, $itemname)
 * @param $formname
 * @param $itemname
 * @return boolean
 */
function isChecked($formname, $itemname){
        return document.forms[$formname].elements[$itemname].checked;
}

/**
 * isChecked($formname, $itemname)
 * @param $id
 * @return boolean
 */
function isCheckedById($id){
	var ret = "false";
	var obj = document.getElementById($id);
	if (obj != null){
        ret = obj.checked;
	}
    return ret;
}

/**
 * getSelectedIndex($formname, $itemname)
 * @param $formname
 * @param $itemname
 * @return Integer SelectedIndex, -1 
 */
function getSelectedIndex($formname, $itemname){
        return document.forms[$formname].elements[$itemname].selectedIndex;
}

function getSelectedValue($formname, $itemname){
        return document.forms[$formname].elements[$itemname].options[document.forms[$formname].elements[$itemname].selectedIndex].value;
}

/**
 * getSelect($selectid, $selected_value)
 * @param $selectid
 * @param $selected_value
 * @return bool $ret 
 */
function changeSelect($selectid, $selected_value){
	ret = false;
	options = document.getElementById($selectid).getElementsByTagName('option');
	for(i=0; i<options.length;i++){
		if(options[i].value == $selected_value){	// 完全一致
//		if($selected_value.indexOf(options[i].value) != -1){	// あいまい一致（文字列に含む）
			options[i].selected = true;
			$ret = true; 
			break;
		}
	}
	return ret;
}

/**
 * setDisabledALL()
 */
function undisabled() {
        for(var i=0;i<document.forms.length;i++)
        for(var j=0;j<document.forms[i].elements.length;j++)document.forms[i].elements[j].disabled=true;void(0);
}

/**
 * setUndisabledALL()
 */
function undisabled() {
        for(var i=0;i<document.forms.length;i++)
        for(var j=0;j<document.forms[i].elements.length;j++)document.forms[i].elements[j].disabled=false;void(0);
}

/**
 * setDisabled($formname, $itemname)
 * @param $formname
 * @param $itemname
 */
function setDisabled($formname, $itemname) {
        document.forms[$formname].elements[$itemname].disabled=true;
}

/**
 * setUndisabled($formname, $itemname)
 * @param $formname
 * @param $itemname
 */
function setUndisabled($formname, $itemname) {
        document.forms[$formname].elements[$itemname].disabled=false;
}

/**
 * setDisabled($id)
 * @param $id
 */
function setDisabled($id) {
	var obj = document.getElementById($id);
	if (obj != null){
        obj.disabled=true;void(0);
	}
}

/**
 * setUndisabled($id)
 * @param $id
 */
function setUndisabled($id) {
	var obj = document.getElementById($id);
	if (obj != null){
        obj.disabled=false;void(0);
	}
}

/**
 * getFilePath($id)
 * @param $id
 */
function getFilePath($id) {
    return document.getElementById($id).value;
}

/**
 * getFileExtension($id)
 * @param $id
 */
function getFileExtension($id) {
    var filepath = getFilePath($id);
    if (filepath == ""){
		return "";
	}
    var ext = "";
    if ((n = filepath.lastIndexOf(".")) != -1) {
        ext = filepath.substring(n+1);
    }
    return ext;
}

/**
 * setValue($id)
 * @param $id
 */
function setValue($id, $value) {
    document.getElementById($id).value = $value;
}

/**
 * upper($id)
 * @param $id
 */
function upper($id){
 	var obj = document.getElementById($id);
	if (obj != null){
		obj.value = obj.value.toUpperCase();
	}
}
/**
 * lower($id)
 * @param $id
 */
function lower($id){
	var obj = document.getElementById($id);
	if (obj != null){
		obj.value = obj.value.toLowerCase();
	}
}


/**
 * upper($formname, $itemname)
 * @param $formname
 * @param $itemname
function upper($formname, $itemname){
	var obj = document.forms[$formname].elements[$itemname];
	if (obj != null){
		obj.value = obj.value.toUpperCase();
	}
}
 */

/**
 * lower($formname, $itemname)
 * @param $formname
 * @param $itemname
function lower($formname, $itemname){
	var obj = document.forms[$formname].elements[$itemname];
	if (obj != null){
		obj.value = obj.value.toLowerCase();
	}
}
*/


/**
 * isset($data)
 * @param $data
 * @param $boolean
 **/
function isset($data){
    return (typeof($data) != 'undefined');
}


/**
 * showAll($name)
 * @param $name
 **/
function showAll($name) {
	var elements = document.getElementsByName($name);		
	for(i=0; i<elements.length;i++){
		var obj = elements[i];
		obj.style.display = "";
	}
}

/**
 * hideAll($name)
 * @param $name
 **/
function hideAll($name) {
	var elements = document.getElementsByName($name);		
	for(i=0; i<elements.length;i++){
		var obj = elements[i];
		obj.style.display = "none";
	}
}


