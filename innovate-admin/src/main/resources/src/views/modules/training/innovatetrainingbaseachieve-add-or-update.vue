<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="基地名称" prop="trainingBaseName">
      <el-input v-model="dataForm.trainingBaseName" placeholder="基地名称"></el-input>
    </el-form-item>
    <el-form-item label="材料年度" prop="materialYear">
      <el-input v-model="dataForm.materialYear" placeholder="材料年度"></el-input>
    </el-form-item>
    <el-form-item label="材料类型" prop="materialType">
      <el-input v-model="dataForm.materialType" placeholder="材料类型"></el-input>
    </el-form-item>
    <el-form-item label="材料类型id" prop="materialTypeId">
      <el-input v-model="dataForm.materialTypeId" placeholder="材料类型id"></el-input>
    </el-form-item>
    <el-form-item label="实训基地id" prop="trainingBaseId">
      <el-input v-model="dataForm.trainingBaseId" placeholder="实训基地id"></el-input>
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
          trainingAchieveId: 0,
          trainingBaseName: '',
          materialYear: '',
          materialType: '',
          materialTypeId: '',
          trainingBaseId: '',
          isDel: ''
        },
        dataRule: {
          trainingBaseName: [
            { required: true, message: '基地名称不能为空', trigger: 'blur' }
          ],
          materialYear: [
            { required: true, message: '材料年度不能为空', trigger: 'blur' }
          ],
          materialType: [
            { required: true, message: '材料类型不能为空', trigger: 'blur' }
          ],
          materialTypeId: [
            { required: true, message: '材料类型id不能为空', trigger: 'blur' }
          ],
          trainingBaseId: [
            { required: true, message: '实训基地id不能为空', trigger: 'blur' }
          ],
          isDel: [
            { required: true, message: '是否删除不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.trainingAchieveId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.trainingAchieveId) {
            this.$http({
              url: this.$http.adornUrl(`/training/innovatetrainingbaseachieve/info/${this.dataForm.trainingAchieveId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.trainingBaseName = data.innovatetrainingbaseachieve.trainingBaseName
                this.dataForm.materialYear = data.innovatetrainingbaseachieve.materialYear
                this.dataForm.materialType = data.innovatetrainingbaseachieve.materialType
                this.dataForm.materialTypeId = data.innovatetrainingbaseachieve.materialTypeId
                this.dataForm.trainingBaseId = data.innovatetrainingbaseachieve.trainingBaseId
                this.dataForm.isDel = data.innovatetrainingbaseachieve.isDel
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
              url: this.$http.adornUrl(`/training/innovatetrainingbaseachieve/${!this.dataForm.trainingAchieveId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'trainingAchieveId': this.dataForm.trainingAchieveId || undefined,
                'trainingBaseName': this.dataForm.trainingBaseName,
                'materialYear': this.dataForm.materialYear,
                'materialType': this.dataForm.materialType,
                'materialTypeId': this.dataForm.materialTypeId,
                'trainingBaseId': this.dataForm.trainingBaseId,
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
