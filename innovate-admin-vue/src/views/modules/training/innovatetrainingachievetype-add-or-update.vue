<template>
  <el-dialog
    :title="!dataForm.materialTypeId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="实训成果类型" prop="trainingAchieveType" label-width="160px" >
      <el-input v-model="dataForm.trainingAchieveType" placeholder="实训成果类型"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" :loading="loading" :disabled="loading">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        loading: false,
        visible: false,
        dataForm: {
          materialTypeId: 0,
          trainingAchieveType: ''
        },
        dataRule: {
          trainingAchieveType: [
            { required: true, message: '实训成果类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.loading = false
        this.dataForm.materialTypeId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.materialTypeId) {
            this.$http({
              url: this.$http.adornUrl(`/training/innovatetrainingachievetype/info/${this.dataForm.materialTypeId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.trainingAchieveType = data.innovateTrainingAchieveType.trainingAchieveType
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.loading = true
            this.$http({
              url: this.$http.adornUrl(`/training/innovatetrainingachievetype/${!this.dataForm.materialTypeId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'materialTypeId': this.dataForm.materialTypeId || undefined,
                'trainingAchieveType': this.dataForm.trainingAchieveType
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
                this.loading = fasle
              }
            })
          }
        })
      }
    }
  }
</script>
