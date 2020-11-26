<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="模糊id P-企业项目表id  A-企业合作表id" prop="functionId">
      <el-input v-model="dataForm.functionId" placeholder="模糊id P-企业项目表id  A-企业合作表id"></el-input>
    </el-form-item>
    <el-form-item label="附件名称" prop="attachName">
      <el-input v-model="dataForm.attachName" placeholder="附件名称"></el-input>
    </el-form-item>
    <el-form-item label="附件路径" prop="attachPath">
      <el-input v-model="dataForm.attachPath" placeholder="附件路径"></el-input>
    </el-form-item>
    <el-form-item label="上传时间" prop="attachTime">
      <el-input v-model="dataForm.attachTime" placeholder="上传时间"></el-input>
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
          materialsId: 0,
          functionId: '',
          attachName: '',
          attachPath: '',
          attachTime: '',
          isDel: ''
        },
        dataRule: {
          functionId: [
            { required: true, message: '模糊id P-企业项目表id  A-企业合作表id不能为空', trigger: 'blur' }
          ],
          attachName: [
            { required: true, message: '附件名称不能为空', trigger: 'blur' }
          ],
          attachPath: [
            { required: true, message: '附件路径不能为空', trigger: 'blur' }
          ],
          attachTime: [
            { required: true, message: '上传时间不能为空', trigger: 'blur' }
          ],
          isDel: [
            { required: true, message: '是否删除不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.materialsId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.materialsId) {
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovatecooperationmaterials/info/${this.dataForm.materialsId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.functionId = data.innovatecooperationmaterials.functionId
                this.dataForm.attachName = data.innovatecooperationmaterials.attachName
                this.dataForm.attachPath = data.innovatecooperationmaterials.attachPath
                this.dataForm.attachTime = data.innovatecooperationmaterials.attachTime
                this.dataForm.isDel = data.innovatecooperationmaterials.isDel
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
              url: this.$http.adornUrl(`/cooperation/innovatecooperationmaterials/${!this.dataForm.materialsId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'materialsId': this.dataForm.materialsId || undefined,
                'functionId': this.dataForm.functionId,
                'attachName': this.dataForm.attachName,
                'attachPath': this.dataForm.attachPath,
                'attachTime': this.dataForm.attachTime,
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
