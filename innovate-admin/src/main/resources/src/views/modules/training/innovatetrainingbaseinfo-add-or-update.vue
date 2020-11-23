<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="实习实训基地名称" prop="trainingBaseName">
      <el-input v-model="dataForm.trainingBaseName" placeholder="实习实训基地名称"></el-input>
    </el-form-item>
    <el-form-item label="所属二级学院" prop="instituteId">
      <el-input v-model="dataForm.instituteId" placeholder="所属二级学院"></el-input>
    </el-form-item>
    <el-form-item label="建立时间" prop="baseCreateTime">
      <el-input v-model="dataForm.baseCreateTime" placeholder="建立时间"></el-input>
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
          trainingBaseId: 0,
          trainingBaseName: '',
          instituteId: '',
          baseCreateTime: '',
          isDel: ''
        },
        dataRule: {
          trainingBaseName: [
            { required: true, message: '实习实训基地名称不能为空', trigger: 'blur' }
          ],
          instituteId: [
            { required: true, message: '所属二级学院不能为空', trigger: 'blur' }
          ],
          baseCreateTime: [
            { required: true, message: '建立时间不能为空', trigger: 'blur' }
          ],
          isDel: [
            { required: true, message: '是否删除不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.trainingBaseId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.trainingBaseId) {
            this.$http({
              url: this.$http.adornUrl(`/training/innovatetrainingbaseinfo/info/${this.dataForm.trainingBaseId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.trainingBaseName = data.innovatetrainingbaseinfo.trainingBaseName
                this.dataForm.instituteId = data.innovatetrainingbaseinfo.instituteId
                this.dataForm.baseCreateTime = data.innovatetrainingbaseinfo.baseCreateTime
                this.dataForm.isDel = data.innovatetrainingbaseinfo.isDel
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
              url: this.$http.adornUrl(`/training/innovatetrainingbaseinfo/${!this.dataForm.trainingBaseId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'trainingBaseId': this.dataForm.trainingBaseId || undefined,
                'trainingBaseName': this.dataForm.trainingBaseName,
                'instituteId': this.dataForm.instituteId,
                'baseCreateTime': this.dataForm.baseCreateTime,
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
