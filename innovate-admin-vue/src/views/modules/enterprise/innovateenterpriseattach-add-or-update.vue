<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="模糊id" prop="functionId">
      <el-input v-model="dataForm.functionId" placeholder="模糊id"></el-input>
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
    <el-form-item label="附件类型（1、入驻材料 2、项目材料 3、企业成果材料）" prop="attachType">
      <el-input v-model="dataForm.attachType" placeholder="附件类型（1、入驻材料 2、项目材料 3、企业成果材料）"></el-input>
    </el-form-item>
    <el-form-item label="是否删除" prop="isDel">
      <el-input v-model="dataForm.isDel" placeholder="是否删除"></el-input>
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
          attachId: 0,
          functionId: '',
          attachName: '',
          attachPath: '',
          attachTime: '',
          attachType: '',
          isDel: ''
        },
        dataRule: {
          functionId: [
            { required: true, message: '模糊id不能为空', trigger: 'blur' }
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
          attachType: [
            { required: true, message: '附件类型（1、入驻材料 2、项目材料 3、企业成果材料）不能为空', trigger: 'blur' }
          ],
          isDel: [
            { required: true, message: '是否删除不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.loading = false
        this.dataForm.attachId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.attachId) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/innovateenterpriseattach/info/${this.dataForm.attachId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.functionId = data.innovateenterpriseattach.functionId
                this.dataForm.attachName = data.innovateenterpriseattach.attachName
                this.dataForm.attachPath = data.innovateenterpriseattach.attachPath
                this.dataForm.attachTime = data.innovateenterpriseattach.attachTime
                this.dataForm.attachType = data.innovateenterpriseattach.attachType
                this.dataForm.isDel = data.innovateenterpriseattach.isDel
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
              url: this.$http.adornUrl(`/enterprise/innovateenterpriseattach/${!this.dataForm.attachId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'attachId': this.dataForm.attachId || undefined,
                'functionId': this.dataForm.functionId,
                'attachName': this.dataForm.attachName,
                'attachPath': this.dataForm.attachPath,
                'attachTime': this.dataForm.attachTime,
                'attachType': this.dataForm.attachType,
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
                this.loading = false
              }
            })
          }
        })
      }
    }
  }
</script>
