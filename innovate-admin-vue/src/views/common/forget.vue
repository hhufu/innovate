<template>
  <el-dialog
    :title="'账号/密码找回'"
    :close-on-click-modal="false"
    width="35vw"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px" status-icon>
      <el-form-item label="邮箱" prop="email">
        <el-row :gutter="10">
          <el-col :span="15">
            <el-input v-model="dataForm.email" placeholder="请输入邮箱地址"></el-input>
          </el-col>
        </el-row>
      </el-form-item >
        <el-form-item label="验证码" prop="captcha">
          <el-row :gutter="10">
            <el-col :span="15">
              <el-input type="text" v-model="dataForm.captcha" placeholder="请输入4位验证码"></el-input>
            </el-col>
            <el-col :span="1">
              <el-button @click="getCaptchaSubmit()" type="success" :disabled="codeDisable" v-if="codeDisable==false">获取验证码</el-button>
              <el-button @click="getCaptchaSubmit()" type="success" :disabled="codeDisable" v-if="codeDisable==true">{{codeSendMsg}}</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-row :gutter="10">
          <el-col :span="15">
          <el-input type="password" v-model="dataForm.newPassword"></el-input>
          </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-row :gutter="10">
          <el-col :span="15">
          <el-input type="password" v-model="dataForm.confirmPassword"></el-input>
          </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    <span slot="footer" class="dialog-footer" >
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      </span>
  </el-dialog>
</template>

<script>
import { isEmail } from '@/utils/validate'
  export default {
    data () {
      var validateConfirmPassword = (rule, value, callback) => {
        if (this.dataForm.newPassword !== value) {
          callback(new Error('确认密码与新密码不一致'))
        } else {
          callback()
        }
      }
      var validateEmail = (rule, value, callback) => {
        if (!isEmail(value)) {
          callback(new Error('邮箱格式错误'))
        } else {
          callback()
        }
      }
      return {
        visible: false,
        registerVisible: false,
        loginLoading: false,
        codeDisable: false,
        codeDiv: false,
        codeSendMsg: '重新发送',
        username: '',
        time: 0,
        user: '',
        dataForm: {
          email: '',
          captcha: '',
          newPassword: '',
          confirmPassword: ''
        },
        instituteList: this.$store.state.user.institute,
        dataRule: {
          email: [
            { required: true, message: '邮箱不能为空', trigger: 'blur' },
            { validator: validateEmail, trigger: 'blur' }
          ],
          captcha: [
            { required: true, message: '验证码不能为空', trigger: 'blur' }
          ],
          newPassword: [
            { required: true, message: '新密码不能为空', trigger: 'blur' }
          ],
          confirmPassword: [
            { required: true, message: '确认密码不能为空', trigger: 'blur' },
            { validator: validateConfirmPassword, trigger: 'blur' }
          ]
        },
        captchaPath: ''
      }
    },
    components: {
    },
    created () {
    },
    mounted () {
    },
    methods: {
      init () {
        this.visible = true
        this.$refs['dataForm'].resetFields()
      },
      // 获取验证码
      getCaptchaSubmit () {
        if (isEmail(this.dataForm.email)) {
          this.codeDisable = true
          this.time = 60
          this.timer()
          this.$http({
            url: this.$http.adornUrl(`/app/sendEmail`),
            method: 'get',
            params: this.$http.adornParams({
              'email': this.dataForm.email
            })
          }).then(({data}) => {
            console.log(data)
            this.user = data.user
            if (this.user === null) {
              this.$message.error('邮箱不存在！')
              this.codeDisable = false
            } else {
                this.$message.success('邮件已发送，请查收')
            }
          })
        } else {
          this.$message.error('请输入正确的邮箱地址！')
        }
      },
      // 60S倒计时
      timer () {
        if (this.time > 0) {
          this.time--
          this.codeSendMsg = this.time + 's后重新获取'
          setTimeout(this.timer, 1000)
        } else {
          this.time = 0
          this.codeSendMsg = '获取验证码'
          this.codeDisable = false
        }
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/app/change`),
              method: 'post',
              data: this.$http.adornData({
                'password': this.dataForm.newPassword,
                'captcha': this.dataForm.captcha,
                'email': this.dataForm.email
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
