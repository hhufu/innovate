<template>
  <el-dialog
    :title="!dataForm.authenticationId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
      <el-form-item label="企业名称" prop="enterpriseName">
        <el-input v-model="dataForm.enterpriseName" placeholder="企业名称"></el-input>
      </el-form-item>
      <el-form-item label="法人代表" prop="corporateRepresentative">
        <el-input v-model="dataForm.corporateRepresentative" placeholder="法人代表"></el-input>
      </el-form-item>
      <el-form-item label="统一信用代码" prop="creditCode">
        <el-input v-model="dataForm.creditCode" placeholder="统一信用代码"></el-input>
      </el-form-item>
      <el-form-item label="企业类型" prop="enterpriseType">
        <el-input v-model="dataForm.enterpriseType" placeholder="企业类型"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          authenticationId: 0,
          enterpriseName: '',
          corporateRepresentative: '',
          creditCode: '',
          enterpriseType: ''
        },
        dataRule: {
          enterpriseName: [
            { required: true, message: '企业名称不能为空', trigger: 'blur' }
          ],
          corporateRepresentative: [
            { required: true, message: '法人代表不能为空', trigger: 'blur' }
          ],
          creditCode: [
            { required: true, message: '统一信用代码不能为空', trigger: 'blur' }
          ],
          enterpriseType: [
            { required: true, message: '企业类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.authenticationId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.authenticationId) {
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovateregisterauthentication/info/${this.dataForm.authenticationId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                 // this.dataForm = data.innovateRegisterAuthentication
                this.dataForm.enterpriseName = data.innovateRegisterAuthentication.enterpriseName
                this.dataForm.corporateRepresentative = data.innovateRegisterAuthentication.corporateRepresentative
                this.dataForm.creditCode = data.innovateRegisterAuthentication.creditCode
                this.dataForm.enterpriseType = data.innovateRegisterAuthentication.enterpriseType
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovateregisterauthentication/${!this.dataForm.authenticationId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'authenticationId': this.dataForm.authenticationId || undefined,
                'enterpriseName': this.dataForm.enterpriseName,
                'corporateRepresentative': this.dataForm.corporateRepresentative,
                'creditCode': this.dataForm.creditCode,
                'enterpriseType': this.dataForm.enterpriseType
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
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
