<template>
  <el-dialog
    :title="!dataForm.awardProjectTypeId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="12rem" style="width: 94%; margin: 0 auto">
    <el-form-item label="获奖项目类型" prop="awardProjectType">
      <el-input v-model="dataForm.awardProjectType" placeholder="获奖项目类型"></el-input>
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
          awardProjectTypeId: 0,
          awardProjectType: ''
        },
        dataRule: {
          awardProjectType: [
            { required: true, message: '获奖项目类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        debugger
        this.loading = false
        this.dataForm.awardProjectTypeId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.awardProjectTypeId) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/innovateawardprojecttype/info/${this.dataForm.awardProjectTypeId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.awardProjectType = data.innovateAwardProjectType.awardProjectType
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
              url: this.$http.adornUrl(`/enterprise/innovateawardprojecttype/${!this.dataForm.awardProjectTypeId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'awardProjectTypeId': this.dataForm.awardProjectTypeId || undefined,
                'awardProjectType': this.dataForm.awardProjectType
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
                this.loading = false
              }
            })
          }
        })
      }
    }
  }
</script>
