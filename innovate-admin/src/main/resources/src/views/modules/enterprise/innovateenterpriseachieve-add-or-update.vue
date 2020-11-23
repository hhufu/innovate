<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="企业 id" prop="enterpriseId">
      <el-input v-model="dataForm.enterpriseId" placeholder="企业 id"></el-input>
    </el-form-item>
    <el-form-item label="企业名称" prop="enterpriseName">
      <el-input v-model="dataForm.enterpriseName" placeholder="企业名称"></el-input>
    </el-form-item>
    <el-form-item label="负责人" prop="enterpriseDirector">
      <el-input v-model="dataForm.enterpriseDirector" placeholder="负责人"></el-input>
    </el-form-item>
    <el-form-item label="用户id" prop="enterpriseUserId">
      <el-input v-model="dataForm.enterpriseUserId" placeholder="用户id"></el-input>
    </el-form-item>
    <el-form-item label="获奖名称（项目名称）" prop="awardProjectName">
      <el-input v-model="dataForm.awardProjectName" placeholder="获奖名称（项目名称）"></el-input>
    </el-form-item>
    <el-form-item label="获奖（得）时间" prop="awardTime">
      <el-input v-model="dataForm.awardTime" placeholder="获奖（得）时间"></el-input>
    </el-form-item>
    <el-form-item label="类型（应用成果转化/获奖/著作权/企业证书）" prop="awardProjectType">
      <el-input v-model="dataForm.awardProjectType" placeholder="类型（应用成果转化/获奖/著作权/企业证书）"></el-input>
    </el-form-item>
    <el-form-item label="所属二级学院" prop="instituteId">
      <el-input v-model="dataForm.instituteId" placeholder="所属二级学院"></el-input>
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
          enterpAchieveId: 0,
          enterpriseId: '',
          enterpriseName: '',
          enterpriseDirector: '',
          enterpriseUserId: '',
          awardProjectName: '',
          awardTime: '',
          awardProjectType: '',
          instituteId: '',
          isDel: ''
        },
        dataRule: {
          enterpriseId: [
            { required: true, message: '企业 id不能为空', trigger: 'blur' }
          ],
          enterpriseName: [
            { required: true, message: '企业名称不能为空', trigger: 'blur' }
          ],
          enterpriseDirector: [
            { required: true, message: '负责人不能为空', trigger: 'blur' }
          ],
          enterpriseUserId: [
            { required: true, message: '用户id不能为空', trigger: 'blur' }
          ],
          awardProjectName: [
            { required: true, message: '获奖名称（项目名称）不能为空', trigger: 'blur' }
          ],
          awardTime: [
            { required: true, message: '获奖（得）时间不能为空', trigger: 'blur' }
          ],
          awardProjectType: [
            { required: true, message: '类型（应用成果转化/获奖/著作权/企业证书）不能为空', trigger: 'blur' }
          ],
          instituteId: [
            { required: true, message: '所属二级学院不能为空', trigger: 'blur' }
          ],
          isDel: [
            { required: true, message: '是否删除不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.enterpAchieveId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.enterpAchieveId) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/innovateenterpriseachieve/info/${this.dataForm.enterpAchieveId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.enterpriseId = data.innovateenterpriseachieve.enterpriseId
                this.dataForm.enterpriseName = data.innovateenterpriseachieve.enterpriseName
                this.dataForm.enterpriseDirector = data.innovateenterpriseachieve.enterpriseDirector
                this.dataForm.enterpriseUserId = data.innovateenterpriseachieve.enterpriseUserId
                this.dataForm.awardProjectName = data.innovateenterpriseachieve.awardProjectName
                this.dataForm.awardTime = data.innovateenterpriseachieve.awardTime
                this.dataForm.awardProjectType = data.innovateenterpriseachieve.awardProjectType
                this.dataForm.instituteId = data.innovateenterpriseachieve.instituteId
                this.dataForm.isDel = data.innovateenterpriseachieve.isDel
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
              url: this.$http.adornUrl(`/enterprise/innovateenterpriseachieve/${!this.dataForm.enterpAchieveId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'enterpAchieveId': this.dataForm.enterpAchieveId || undefined,
                'enterpriseId': this.dataForm.enterpriseId,
                'enterpriseName': this.dataForm.enterpriseName,
                'enterpriseDirector': this.dataForm.enterpriseDirector,
                'enterpriseUserId': this.dataForm.enterpriseUserId,
                'awardProjectName': this.dataForm.awardProjectName,
                'awardTime': this.dataForm.awardTime,
                'awardProjectType': this.dataForm.awardProjectType,
                'instituteId': this.dataForm.instituteId,
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
