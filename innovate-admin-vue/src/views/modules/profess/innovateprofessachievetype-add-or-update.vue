<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
    <el-form-item label="实训成果类型" prop="professAchieveType">
      <el-input v-model="dataForm.professAchieveType" placeholder="实训成果类型"></el-input>
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
          professAchieveTypeId: 0,
          professAchieveType: ''
        },
        dataRule: {
          professAchieveType: [
            { required: true, message: '实训成果类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.professAchieveTypeId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.professAchieveTypeId) {
            this.$http({
              url: this.$http.adornUrl(`/profess/innovateprofessachievetype/info/${this.dataForm.professAchieveTypeId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.professAchieveType = data.innovateProfessAchieveType.professAchieveType
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
              url: this.$http.adornUrl(`/profess/innovateprofessachievetype/${!this.dataForm.professAchieveTypeId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'professAchieveTypeId': this.dataForm.professAchieveTypeId || undefined,
                'professAchieveType': this.dataForm.professAchieveType
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
