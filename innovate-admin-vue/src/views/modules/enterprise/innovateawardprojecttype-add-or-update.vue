<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="获奖项目类型" prop="awardProjectjType">
      <el-input v-model="dataForm.awardProjectjType" placeholder="获奖项目类型"></el-input>
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
          awardProjectjTypeId: 0,
          awardProjectjType: ''
        },
        dataRule: {
          awardProjectjType: [
            { required: true, message: '获奖项目类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.awardProjectjTypeId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.awardProjectjTypeId) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/innovateawardprojecttype/info/${this.dataForm.awardProjectjTypeId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.awardProjectjType = data.innovateawardprojecttype.awardProjectjType
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
              url: this.$http.adornUrl(`/enterprise/innovateawardprojecttype/${!this.dataForm.awardProjectjTypeId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'awardProjectjTypeId': this.dataForm.awardProjectjTypeId || undefined,
                'awardProjectjType': this.dataForm.awardProjectjType
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
