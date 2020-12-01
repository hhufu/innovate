<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
<!--    <el-form-item label="负责人id" prop="professUserId">-->
<!--      <el-input v-model="dataForm.professUserId" placeholder="负责人id"></el-input>-->
<!--    </el-form-item>-->
    <el-form-item label="所属二级学院" prop="instituteId">
      <el-select v-model="dataForm.instituteId" placeholder="负责人所在学院">
        <el-option v-for="item in instituteName" :key="item.instituteId" :label="item.instituteName" :value="item.instituteId">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="获奖名称" prop="awardAchieName">
      <el-input v-model="dataForm.awardAchieName" placeholder="获奖(项目）名称"></el-input>
    </el-form-item>
    <el-form-item label="负责人" prop="professAchieDirector">
      <el-input v-model="dataForm.professAchieDirector" placeholder="负责人"></el-input>
    </el-form-item>
    <el-form-item label="团队成员" prop="teamMenber">
      <el-input v-model="dataForm.teamMenber" placeholder="团队成员"></el-input>
    </el-form-item>
    <el-form-item label="获得时间" prop="awardTime">
      <el-date-picker
        v-model="dataForm.awardTime"
        type="datetime"
        format="yyyy-MM-dd HH:ss:mm"
        value-format="timestamp"
        placeholder="获得时间">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="成果类型" prop="professAchieveType">
      <el-select v-model="dataForm.professAchieveType" placeholder="" @change="changeProfessAchieveType">
        <el-option v-for="item in professAchieveType" :key="item.professAchieveType" :label="item.professAchieveType" :value="item.professAchieveType">
        </el-option>
      </el-select>
    </el-form-item>
<!--    <el-form-item label="专创成果类型id" prop="professAchieveTypeId">-->
<!--      <el-input v-model="dataForm.professAchieveTypeId" placeholder="专创成果类型id"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="是否删除" prop="isDel">-->
<!--      <el-input v-model="dataForm.isDel" placeholder="是否删除"></el-input>-->
<!--    </el-form-item>-->
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
          professAchieveId: 0,
          professUserId: '',
          instituteId: '',
          awardAchieName: '',
          professAchieDirector: '',
          teamMenber: '',
          awardTime: '',
          professAchieveType: '',
          professAchieveTypeId: '',
          isDel: ''
        },
        dataRule: {
          professUserId: [
            { required: true, message: '负责人id不能为空', trigger: 'blur' }
          ],
          instituteId: [
            { required: true, message: '所属二级学院不能为空', trigger: 'blur' }
          ],
          awardAchieName: [
            { required: true, message: '获奖(项目）名称不能为空', trigger: 'blur' }
          ],
          professAchieDirector: [
            { required: true, message: '负责人不能为空', trigger: 'blur' }
          ],
          teamMenber: [
            { required: true, message: '团队成员不能为空', trigger: 'blur' }
          ],
          awardTime: [
            { required: true, message: '获得时间不能为空', trigger: 'blur' }
          ],
          professAchieveType: [
            { required: true, message: '专创成果类型（奖励、立项、教改项目、教改论文、教学成果奖、教学过程材料、教学效果）不能为空', trigger: 'blur' }
          ]

        },
        instituteName: [], // 学院列表
        professAchieveType: [] // 成果类型
      }
    },
    methods: {
      init (id) {
        this.dataForm.professAchieveId = id || 0
        this.visible = true
        this.getInstituteName()
        this.getprofessAchieveType()
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.professAchieveId) {
            this.$http({
              url: this.$http.adornUrl(`/profess/innovateprofessachieve/info/${this.dataForm.professAchieveId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                // this.dataForm.professUserId = data.innovateProfessAchieve.professUserId
                this.dataForm.instituteId = data.innovateProfessAchieve.instituteId
                this.dataForm.awardAchieName = data.innovateProfessAchieve.awardAchieName
                this.dataForm.professAchieDirector = data.innovateProfessAchieve.professAchieDirector
                this.dataForm.teamMenber = data.innovateProfessAchieve.teamMenber
                this.dataForm.awardTime = data.innovateProfessAchieve.awardTime
                this.dataForm.professAchieveType = data.innovateProfessAchieve.professAchieveType
                this.dataForm.professAchieveTypeId = data.innovateProfessAchieve.professAchieveTypeId
                // this.dataForm.isDel = data.innovateProfessAchieve.isDel
              }
            })
          }
        })
      },
      changeProfessAchieveType(e) {
          let list = this.professAchieveType.filter(item => {
            return item.professAchieveType.indexOf(e)
          })
        this.dataForm.professAchieveTypeId = list[0].professAchieveTypeId
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/profess/innovateprofessachieve/${!this.dataForm.professAchieveId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'professAchieveId': this.dataForm.professAchieveId || undefined,
                // 'professUserId': this.dataForm.professUserId,
                'instituteId': this.dataForm.instituteId,
                'awardAchieName': this.dataForm.awardAchieName,
                'professAchieDirector': this.dataForm.professAchieDirector,
                'teamMenber': this.dataForm.teamMenber,
                'awardTime': this.dataForm.awardTime,
                'professAchieveType': this.dataForm.professAchieveType,
                'professAchieveTypeId': this.dataForm.professAchieveTypeId
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
      },
      getInstituteName () {
        debugger
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/institute/all`),
          method: 'get'
        }).then(({data}) => {
          console.log(data)
          if (data && data.code === 0) {
            console.log(data.institute)
            this.instituteName = data.institute
          }
        })
      },
      getprofessAchieveType () {
        debugger
        this.$http({
          url: this.$http.adornUrl(`/profess/innovateprofessachievetype/all`),
          method: 'get'
        }).then(({data}) => {
          console.log(data)
          if (data && data.code === 0) {
            console.log(data.professAchieveType)
            this.professAchieveType = data.professAchieveType
          }
        })
      }
    }
  }
</script>
