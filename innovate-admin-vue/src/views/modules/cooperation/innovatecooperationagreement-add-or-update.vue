<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="企业名称" prop="enterpriseName">
      <el-input v-model="dataForm.enterpriseName" placeholder="企业名称"></el-input>
    </el-form-item>
    <el-form-item label="二级学院 innovate_sys_institute主键" prop="instituteId">
      <el-input v-model="dataForm.instituteId" placeholder="二级学院 innovate_sys_institute主键"></el-input>
    </el-form-item>
    <el-form-item label="年度" prop="agreementYear">
      <el-input v-model="dataForm.agreementYear" placeholder="年度"></el-input>
    </el-form-item>
    <el-form-item label="协议时间" prop="agreementTime">
      <el-input v-model="dataForm.agreementTime" placeholder="协议时间"></el-input>
    </el-form-item>
    <el-form-item label="协议材料" prop="agreementMaterials">
      <el-input v-model="dataForm.agreementMaterials" placeholder="协议材料"></el-input>
    </el-form-item>
    <el-form-item label="企业记录" prop="enterpriseRecords">
      <el-input v-model="dataForm.enterpriseRecords" placeholder="企业记录"></el-input>
    </el-form-item>
    <el-form-item label="创建者" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="创建者"></el-input>
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
          enterpriseId: 0,
          enterpriseName: '',
          instituteId: '',
          agreementYear: '',
          agreementTime: '',
          agreementMaterials: '',
          enterpriseRecords: '',
          userId: ''
        },
        dataRule: {
          enterpriseName: [
            { required: true, message: '企业名称不能为空', trigger: 'blur' }
          ],
          instituteId: [
            { required: true, message: '二级学院 innovate_sys_institute主键不能为空', trigger: 'blur' }
          ],
          agreementYear: [
            { required: true, message: '年度不能为空', trigger: 'blur' }
          ],
          agreementTime: [
            { required: true, message: '协议时间不能为空', trigger: 'blur' }
          ],
          agreementMaterials: [
            { required: true, message: '协议材料不能为空', trigger: 'blur' }
          ],
          enterpriseRecords: [
            { required: true, message: '企业记录不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '创建者不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.enterpriseId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.enterpriseId) {
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovatecooperationagreement/info/${this.dataForm.enterpriseId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.enterpriseName = data.innovatecooperationagreement.enterpriseName
                this.dataForm.instituteId = data.innovatecooperationagreement.instituteId
                this.dataForm.agreementYear = data.innovatecooperationagreement.agreementYear
                this.dataForm.agreementTime = data.innovatecooperationagreement.agreementTime
                this.dataForm.agreementMaterials = data.innovatecooperationagreement.agreementMaterials
                this.dataForm.enterpriseRecords = data.innovatecooperationagreement.enterpriseRecords
                this.dataForm.userId = data.innovatecooperationagreement.userId
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
              url: this.$http.adornUrl(`/cooperation/innovatecooperationagreement/${!this.dataForm.enterpriseId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'enterpriseId': this.dataForm.enterpriseId || undefined,
                'enterpriseName': this.dataForm.enterpriseName,
                'instituteId': this.dataForm.instituteId,
                'agreementYear': this.dataForm.agreementYear,
                'agreementTime': this.dataForm.agreementTime,
                'agreementMaterials': this.dataForm.agreementMaterials,
                'enterpriseRecords': this.dataForm.enterpriseRecords,
                'userId': this.dataForm.userId
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
