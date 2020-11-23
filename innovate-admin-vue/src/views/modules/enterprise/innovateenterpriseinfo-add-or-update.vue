<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="企业名称" prop="enterpriseName">
      <el-input v-model="dataForm.enterpriseName" placeholder="企业名称"></el-input>
    </el-form-item>
    <el-form-item label="用户ID" prop="enterpriseUserId">
      <el-input v-model="dataForm.enterpriseUserId" placeholder="用户ID"></el-input>
    </el-form-item>
    <el-form-item label="企业负责人姓名" prop="enterpriseDirector">
      <el-input v-model="dataForm.enterpriseDirector" placeholder="企业负责人姓名"></el-input>
    </el-form-item>
    <el-form-item label="负责人所在学院" prop="departmentDirector">
      <el-input v-model="dataForm.departmentDirector" placeholder="负责人所在学院"></el-input>
    </el-form-item>
    <el-form-item label="入驻时间" prop="settledTime">
      <el-input v-model="dataForm.settledTime" placeholder="入驻时间"></el-input>
    </el-form-item>
    <el-form-item label="企业类型" prop="enterpriseType">
      <el-input v-model="dataForm.enterpriseType" placeholder="企业类型"></el-input>
    </el-form-item>
    <el-form-item label="主要经营范围" prop="business Scope">
      <el-input v-model="dataForm.businessScope" placeholder="主要经营范围"></el-input>
    </el-form-item>
    <el-form-item label="审核状态" prop="applyStatus">
      <el-input v-model="dataForm.applyStatus" placeholder="审核状态"></el-input>
    </el-form-item>
    <el-form-item label="是否删除" prop="isDel">
      <el-input v-model="dataForm.isDel" placeholder="是否删除"></el-input>
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
          settledEnterpId: 0,
          enterpriseName: '',
          enterpriseUserId: '',
          enterpriseDirector: '',
          departmentDirector: '',
          settledTime: '',
          enterpriseType: '',
          businessScope: '',
          applyStatus: '',
          isDel: ''
        },
        dataRule: {
          enterpriseName: [
            { required: true, message: '企业名称不能为空', trigger: 'blur' }
          ],
          enterpriseUserId: [
            { required: true, message: '用户ID不能为空', trigger: 'blur' }
          ],
          enterpriseDirector: [
            { required: true, message: '企业负责人姓名不能为空', trigger: 'blur' }
          ],
          departmentDirector: [
            { required: true, message: '负责人所在学院不能为空', trigger: 'blur' }
          ],
          settledTime: [
            { required: true, message: '入驻时间不能为空', trigger: 'blur' }
          ],
          enterpriseType: [
            { required: true, message: '企业类型不能为空', trigger: 'blur' }
          ],
          businessScope: [
            { required: true, message: '主要经营范围不能为空', trigger: 'blur' }
          ],
          applyStatus: [
            { required: true, message: '审核状态不能为空', trigger: 'blur' }
          ],
          isDel: [
            { required: true, message: '是否删除不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.settledEnterpId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.settledEnterpId) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/innovateenterpriseinfo/info/${this.dataForm.settledEnterpId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.enterpriseName = data.innovateenterpriseinfo.enterpriseName
                this.dataForm.enterpriseUserId = data.innovateenterpriseinfo.enterpriseUserId
                this.dataForm.enterpriseDirector = data.innovateenterpriseinfo.enterpriseDirector
                this.dataForm.departmentDirector = data.innovateenterpriseinfo.departmentDirector
                this.dataForm.settledTime = data.innovateenterpriseinfo.settledTime
                this.dataForm.enterpriseType = data.innovateenterpriseinfo.enterpriseType
                this.dataForm.businessScope = data.innovateenterpriseinfo.businessScope
                this.dataForm.applyStatus = data.innovateenterpriseinfo.applyStatus
                this.dataForm.isDel = data.innovateenterpriseinfo.isDel
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
              url: this.$http.adornUrl(`/enterprise/innovateenterpriseinfo/${!this.dataForm.settledEnterpId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'settledEnterpId': this.dataForm.settledEnterpId || undefined,
                'enterpriseName': this.dataForm.enterpriseName,
                'enterpriseUserId': this.dataForm.enterpriseUserId,
                'enterpriseDirector': this.dataForm.enterpriseDirector,
                'departmentDirector': this.dataForm.departmentDirector,
                'settledTime': this.dataForm.settledTime,
                'enterpriseType': this.dataForm.enterpriseType,
                'businessScope': this.dataForm.businessScope,
                'applyStatus': this.dataForm.applyStatus,
                'isDel': this.dataForm.isDel
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
