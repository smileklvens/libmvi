package com.ikang.loginmodule.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.cxz.kotlin.baselibs.utils.CommonUtil.dp2px
import com.ikang.libmvi.util.ext.then
import com.ikang.libutils.utils.StringUtils
import com.ikang.loginmodule.R
import kotlinx.android.synthetic.main.layout_edittext_cear_combination.view.*

/**
 * @author ikang-renwei
 * @Date 2019/12/02 10:45
 * @describe 自定义输入框
 */

class EdittextClearCombination @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    internal var imm: InputMethodManager
    private var typeFace: Typeface? = null
    private var count = 0

    var pwd: Boolean = false
    var number: Boolean = false
    var pwdAndNumber: Boolean = false
    var etIsShowEye: Boolean = false
    var etHint: CharSequence = ""
    var etMaxLength: Int = 20
    var etIsFormatPhone: Boolean = false//是否需要格式化手机号

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_edittext_cear_combination, this, true)
        attrs?.let {
            val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText)
            a.run {
                pwd = getBoolean(R.styleable.ClearEditText_etPwd, false)
                number = getBoolean(R.styleable.ClearEditText_etNumber, false)
                pwdAndNumber = getBoolean(R.styleable.ClearEditText_pwdAndNumber, false)
                etIsShowEye = getBoolean(R.styleable.ClearEditText_etIsShowEye, false)
                etHint = getText(R.styleable.ClearEditText_etHint)
                etMaxLength = getInt(R.styleable.ClearEditText_etMaxLength, 20)
                etIsFormatPhone = getBoolean(R.styleable.ClearEditText_etIsFormatPhone, false)
                recycle()
            }
        }

        //图标
        edittextCearIv.visibility = View.VISIBLE

        //设置提示字
        etHint?.run {
            etInput.hint = this
        }
        if (pwd && number) {//密码&数字
            etInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        }
        if (pwd && !number) {//密码
            etInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        if (number && !pwd) {//无英文账号
            etInput.inputType = InputType.TYPE_CLASS_NUMBER
        }
        if (pwdAndNumber) {//全类型
            etInput.inputType = InputType.TYPE_CLASS_TEXT
        }
        if (etIsShowEye) {//是否显示眼睛
            ivEye.visibility = View.VISIBLE
        } else {
            ivEye.visibility = View.GONE
        }
        //最大位数
        if (etMaxLength as Any != null) {
            etInput.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(etMaxLength))
        }

        typeFace = Typeface.defaultFromStyle(Typeface.NORMAL)

        imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        ivClear.setOnClickListener { etInput.setText("") }

        ivEye.setOnClickListener {
            //若是密码输入，显示“眼睛”不显示“X”
            if (count % 2 == 0) {
                //显示密码
                etInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
//                ivEye.setImageResource(R.drawable.icon_base_eye_open_1)
                ivEye.background = resources.getDrawable(R.color.basic_color_placeholder)
            } else {
                //隐藏密码
                etInput.transformationMethod = PasswordTransformationMethod.getInstance()
//                ivEye.setImageResource(R.drawable.icon_base_eye_close_1)
                ivEye.background = resources.getDrawable(R.color.common_main_color)
            }
            count++
            etInput.setSelection(etInput.text.toString().length)
        }

        //输入框变化监听
        etInput.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                var params = pbUnderline.layoutParams as RelativeLayout.LayoutParams

                if ((s.toString()).isNullOrEmpty()) {
                    pbUnderline.progress = 0
                    params.height = dp2px(getContext(), 0.5f)
                    pbUnderline.layoutParams = params
                } else {
                    params.height = dp2px(getContext(), 2f)
                    pbUnderline.layoutParams = params
                    if (pbUnderline.progress < 100) {
                        object : Thread() {
                            override fun run() {
                                super.run()
                                for (i in 0..20) {
                                    pbUnderline.progress = i * 5
                                    try {
                                        Thread.sleep(5)
                                    } catch (e: InterruptedException) {
                                        e.printStackTrace()
                                    }

                                }
                            }
                        }.start()
                    }
                }

                //判断是否显示清除文字按钮
                if ((s.toString()).isNullOrEmpty()) {
                    if (!etIsShowEye)
                        ivClear.visibility = View.GONE
                    iTextChanged?.textChanged(false)

                    etInput.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    etInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

                    //判断是否合法
                    etIsFormatPhone.then {
                        ((StringUtils.checkMobile(getText()))).then({
                            iTextStandard?.textStandard (true)
                        },{
                            iTextStandard?.textStandard (false)
                        })
                    }
                } else {
                    if (!etIsShowEye)
                        ivClear.visibility = View.VISIBLE
                    iTextChanged?.textChanged(true)

                    etInput.typeface = typeFace
                    etInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f)

                    //判断是否合法
                    etIsFormatPhone.then {
                        ((StringUtils.checkMobile(getText()))).then({
                            iTextStandard?.textStandard (true)
                        },{
                            iTextStandard?.textStandard (false)
                        })
                    }
//                    if (etIsFormatPhone) {
//                        if (iTextStandard != null)
//                            if ((getText()).isNullOrEmpty()) {
//                                iTextStandard!!.textStandard(true)
//                            } else {
//                                iTextStandard!!.textStandard(false)
//                            }
//                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                //当输入类型为纯数字时 格式为344
                if (etIsFormatPhone) {
                    if (charSequence == null || charSequence.length == 0) return
                    val sb = StringBuilder()
                    for (i in 0 until charSequence.length) {
                        if (i != 3 && i != 8 && charSequence[i] == ' ') {
                            continue
                        } else {
                            sb.append(charSequence[i])
                            if ((sb.length == 4 || sb.length == 9) && sb[sb.length - 1] != ' ') {
                                sb.insert(sb.length - 1, ' ')
                            }
                        }
                    }
                    if (sb.toString() != charSequence.toString()) {
                        var index = start + 1
                        if (sb[start] == ' ') {
                            if (before == 0) {
                                index++
                            } else {
                                index--
                            }
                        } else {
                            if (before == 1) {
                                index--
                            }
                        }
                        etInput.setText(sb.toString().trim { it <= ' ' })
                        etInput.setSelection(index)
                    }
                    //当尾部有空格时移除空格
                    if (charSequence.toString() != charSequence.toString().trim { it <= ' ' }) {
                        //移除空格
                        etInput.setText(charSequence.toString().trim { it <= ' ' })
                        //将光标移至文字末尾
                        etInput.setSelection(etInput.text.toString().length)
                    }
                }
            }
        })
    }

    fun getText(): String {
        val edtText = etInput.text.toString()
        //当输入类型为纯数字时 去掉所有空格，包括首尾、中间
        return if (number && !pwd) {
            edtText.replace(" ".toRegex(), "")
        } else edtText
    }

    //接口回调
    private var iTextChanged: ITextChanged? = null
    fun setiTextChanged(iTextChanged: ITextChanged) {
        this.iTextChanged = iTextChanged
    }
    interface ITextChanged {
        fun textChanged(visible: Boolean)
    }

    //判断当前手机号是否合法
    private var iTextStandard: ITextStandardListener? = null
    fun setTextStandardListener(iTextStandard: ITextStandardListener) {
        this.iTextStandard = iTextStandard
    }
    interface ITextStandardListener {
        fun textStandard(visible: Boolean)
    }

}