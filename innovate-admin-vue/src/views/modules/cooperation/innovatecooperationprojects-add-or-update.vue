<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="项目名称" prop="projectName">
      <el-input v-model="dataForm.projectName" placeholder="项目名称"></el-input>
    </el-form-item>
    <el-form-item label="二级学院  学院表主键" prop="instituteId">
      <el-input v-model="dataForm.instituteId" placeholder="二级学院  学院表主键"></el-input>
    </el-form-item>
    <el-form-item label="企业名称" prop="enterpriseName">
      <el-input v-model="dataForm.enterpriseName" placeholder="企业名称"></el-input>
    </el-form-item>
    <el-form-item label="年度" prop="cooperationYear">
      <el-input v-model="dataForm.cooperationYear" placeholder="年度"></el-input>
    </el-form-item>
    <el-form-item label="主持人" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="主持人"></el-input>
    </el-form-item>
    <el-form-item label="起始时间" prop="startTime">
      <el-input v-model="dataForm.startTime" placeholder="起始时间"></el-input>
    </el-form-item>
    <el-form-item label="截止时间" prop="endTime">
      <el-input v-model="dataForm.endTime" placeholder="截止时间"></el-input>
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
          cooperationId: 0,
          projectName: '',
          instituteId: '',
          enterpriseName: '',
          cooperationYear: '',
          userId: '',
          startTime: '',
          endTime: ''
        },
        dataRule: {
          projectName: [
            { required: true, message: '项目名称不能为空', trigger: 'blur' }
          ],
          instituteId: [
            { required: true, message: '二级学院  学院表主键不能为空', trigger: 'blur' }
          ],
          enterpriseName: [
            { required: true, message: '企业名称不能为空', trigger: 'blur' }
          ],
          cooperationYear: [
            { required: true, message: '年度不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '主持人不能为空', trigger: 'blur' }
          ],
          startTime: [
            { required: true, message: '起始时间不能为空', trigger: 'blur' }
          ],
          endTime: [
            { required: true, message: '截止时间不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.cooperationId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.cooperationId) {
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovatecooperationprojects/info/${this.dataForm.cooperationId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.projectName = data.innovatecooperationprojects.projectName
                this.dataForm.instituteId = data.innovatecooperationprojects.instituteId
                this.dataForm.enterpriseName = data.innovatecooperationprojects.enterpriseName
                this.dataForm.cooperationYear = data.innovatecooperationprojects.cooperationYear
                this.dataForm.userId = data.innovatecooperationprojects.userId
                this.dataForm.startTime = data.innovatecooperationprojects.startTime
                this.dataForm.endTime = data.innovatecooperationprojects.endTime
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
              url: this.$http.adornUrl(`/cooperation/innovatecooperationprojects/${!this.dataForm.cooperationId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'cooperationId': this.dataForm.cooperationId || undefined,
                'projectName': this.dataForm.projectName,
                'instituteId': this.dataForm.instituteId,
                'enterpriseName': this.dataForm.enterpriseName,
                'cooperationYear': this.dataForm.cooperationYear,
                'userId': this.dataForm.userId,
                'startTime': this.dataForm.startTime,
                'endTime': this.dataForm.endTime
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
