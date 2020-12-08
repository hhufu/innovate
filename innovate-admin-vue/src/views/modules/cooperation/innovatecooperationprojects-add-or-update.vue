<template>
  <el-dialog
    :title="!dataForm.cooperationId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="项目名称" prop="projectName">
      <el-input v-model="dataForm.projectName" placeholder="项目名称"></el-input>
    </el-form-item>
    <el-form-item label="二级学院" prop="instituteId">
      <el-select v-model="dataForm.instituteId" placeholder="二级学院">
        <el-option v-for="item in instituteName" :key="item.instituteId" :label="item.instituteName" :value="item.instituteId">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="企业名称" prop="enterpriseName">
      <el-select v-model="dataForm.enterpriseName" placeholder="企业名称" @change="changeenter">
        <el-option
          v-for="item in nameList"
          :key="item.authenticationId"
          :label="item.enterpriseName"
          :value="item.enterpriseName">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="年度" prop="cooperationYear">
      <el-date-picker
        v-model="dataForm.cooperationYear"
        type="year"
        format="yyyy"
        value-format="yyyy"
        placeholder="选择年度">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="起始时间" prop="startTime">
      <el-date-picker
        v-model="dataForm.startTime"
        type="date"
        value-format="yyyy-MM-dd  HH:mm:ss"
        format="yyyy-MM-dd"
        placeholder="选择日期">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="截止时间" prop="endTime">
      <el-date-picker
        v-model="dataForm.endTime"
        type="date"
        value-format="yyyy-MM-dd  HH:mm:ss"
        format="yyyy-MM-dd"
        placeholder="选择日期">
      </el-date-picker>
    </el-form-item>
      <el-form-item label="附件要求">
        <template>
          <el-alert
            title=""
            type="success"
            :closable="false"
            :description="fileAskContent">
          </el-alert>
        </template>
      </el-form-item>
      <!--独立附件start-->
      <el-form-item label="相关附件" prop="reportSalesName">
        <el-upload
          class="upload-demo"
          :action="url"
          :data="{textType: textType}"
          :on-success="successHandle1"
          :on-remove="fileRemoveHandler"
          :file-list="fileList">
          <el-button size="small" type="primary">点击上传</el-button>
          <span v-if="attachLists.length === 0" style="color: crimson">*请上传相关附件</span>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" :loading="loading" :disabled="loading">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  class Cooperationachment {
    constructor (file) {
      this.name = file.attachName
      this.url = file.attachPath
      this.file = file
    }
  }
  export default {
    data () {
      return {
        loading: false,
        visible: false,
        url: '',
        fileAskContent: '无',
        fileIsNull: false,
        fileList: [],
        attachLists: [], // 附件列表
        delMaterialsList: [], // 要删除的附件
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
        textType: '',
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
        },
        instituteName: [], // 学院列表
        nameList: [] // 企业名称列表
      }
    },
    methods: {
      init (id) {
        this.dataForm.instituteId = this.$store.state.user.instituteId
        this.loading = false
        this.url = this.$http.adornUrl(`/cooperation/innovatecooperationmaterials/upload?token=${this.$cookie.get('token')}`)
        this.dataForm.cooperationId = id || 0
        this.visible = true
        this.getInstituteList()
        this.$http({
          url: this.$http.adornUrl(`/cooperation/innovateregisterauthentication/list`),
          method: 'get',
          params: this.$http.adornParams({
            isDel: 0
          })
        }).then(({data}) => {
          this.nameList = data.page.list
        })
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.cooperationId) {
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovatecooperationprojects/info/${this.dataForm.cooperationId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                // this.dataForm = data.innovateCooperationProjects
                this.dataForm.projectName = data.innovateCooperationProjectsEntity.projectName
                this.dataForm.instituteId = data.innovateCooperationProjectsEntity.instituteId
                this.dataForm.enterpriseName = data.innovateCooperationProjectsEntity.enterpriseName
                this.dataForm.cooperationYear = data.innovateCooperationProjectsEntity.cooperationYear
                this.dataForm.userId = data.innovateCooperationProjectsEntity.userId
                this.dataForm.startTime = data.innovateCooperationProjectsEntity.startTime
                this.dataForm.endTime = data.innovateCooperationProjectsEntity.endTime
                this.dataForm.userId = data.innovateCooperationProjectsEntity.userId
                this.dataForm.authenticationId = data.innovateCooperationProjectsEntity.authenticationId
                this.attachLists = data.materialsEntityList
                // 附件回显
                let tempFinishAtta = []
                for (var i = 0; i < this.attachLists.length; i++) {
                  tempFinishAtta.push(new Cooperationachment(this.attachLists[i]))
                }
                this.fileList = tempFinishAtta
                this.isTeacherInfoNull()
                this.dataListLoading = false
              }
            }).catch((e) => {})  // length异常抛出
          } else {
            this.reset()
          }
          // 获取文件要求：类型=>1 大创,2 中期检查,3 赛事,4 结题
          this.dataListLoading = true
          this.$http({
            url: this.$http.adornUrl(`/innovate/sys/file/ask/query`),
            method: 'get',
            params: this.$http.adornParams({
              'fileAskType': 13,
              'fileAskTime': new Date().getFullYear()
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.fileAskContent = data.fileAsk == null ? '无' : data.fileAsk.fileAskContent
              this.dataListLoading = false
            }
          })
        })
      },
      reset() {
          this.attachLists = []
          this.fileList = []
          this.delMaterialsList = []
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid && this.attachLists.length > 0) {
            this.loading = true
            this.dataForm.userId = this.$store.state.user.id
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovatecooperationprojects/${!this.dataForm.cooperationId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                cooperationProjectsEntity: this.dataForm,
                delMaterialsList: this.delMaterialsList,
                cooperationMaterialsList: this.attachLists,
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
      },
      getInstituteList () {
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/institute/all`),
          method: 'get'
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.instituteName = data.institute
          }
        })
      },
      // 上传成功
      successHandle1 (response, file, fileList) {
        if (response && response.code === 0) {
          this.attachLists.push(response.cooperationMaterialsEntity)
          this.fileIsNull = false
        } else {
          this.$message.error(response.msg)
        }
      },
      fileRemoveHandler (file, fileList) {
        // 移除attachList中的附件
        let tempFileList = []
        for (var index = 0; index < this.attachLists.length; index++) {
          if (this.attachLists[index].attachName !== file.name) {
            tempFileList.push(this.attachLists[index])
          } else {
            this.delMaterialsList.push(this.attachLists[index])
          }
        }
        this.attachLists = tempFileList
      },
      changeenter(val){
        let List = this.nameList.filter(item => {
          return item.enterpriseName.indexOf(val) > -1
        })
        this.dataForm.authenticationId = List[0].authenticationId
      }
    }
  }
</script>
