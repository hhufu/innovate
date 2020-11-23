<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="专创结合成果id" prop="professAchieveId">
      <el-input v-model="dataForm.professAchieveId" placeholder="专创结合成果id"></el-input>
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
          attachId: 0,
          professAchieveId: '',
          attachName: '',
          attachPath: '',
          attachTime: '',
          isDel: ''
        },
        dataRule: {
          professAchieveId: [
            { required: true, message: '专创结合成果id不能为空', trigger: 'blur' }
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
        this.dataForm.attachId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.attachId) {
            this.$http({
              url: this.$http.adornUrl(`/profess/innovateprofessattach/info/${this.dataForm.attachId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.professAchieveId = data.innovateprofessattach.professAchieveId
                this.dataForm.attachName = data.innovateprofessattach.attachName
                this.dataForm.attachPath = data.innovateprofessattach.attachPath
                this.dataForm.attachTime = data.innovateprofessattach.attachTime
                this.dataForm.isDel = data.innovateprofessattach.isDel
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
              url: this.$http.adornUrl(`/profess/innovateprofessattach/${!this.dataForm.attachId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'attachId': this.dataForm.attachId || undefined,
                'professAchieveId': this.dataForm.professAchieveId,
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
