<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="150px">
    <el-form-item label="填写类型">
      <el-radio v-model="textType" label="1">基础标题</el-radio>
      <el-radio v-model="textType" label="2">积分标准</el-radio>
    </el-form-item>
    <el-form-item v-if="textType == '1'" label="申报类型" prop="participateType">
      <el-select v-model="dataForm.participateType" @change="changeParticipate" style="width: 60%" placeholder="请选择或添加申报类型" default-first-option filterable clearable allow-create>
        <el-option :label="dataForm.participateType"
                   :value="dataForm.participateType"
                   :key="dataForm.participateType"
        >

        </el-option>
      </el-select>
    </el-form-item>
      <el-form-item v-if="textType == '2'" label="申报类型" prop="parentId">
        <el-select v-model="dataForm.parentId" @change="changeParticipate" style="width: 60%" placeholder="请选择或添加申报类型" default-first-option filterable clearable>
          <el-option v-for="item in participateList"
                     :label="item.participateType"
                     :value="item.integralId"
                     :key="item.integralId"
          >

          </el-option>
        </el-select>
      </el-form-item>
    <el-form-item v-if="textType == '2'" label="项目名称" prop="raceGrade">
      <el-input v-model="dataForm.raceGrade" style="width: 60%" placeholder="比赛级别或项目名称" type="textarea" autosize></el-input>
    </el-form-item>
    <el-form-item v-if="textType == '2'" label="奖项等级" prop="prizeGrade">
      <el-input v-model="dataForm.prizeGrade" placeholder="奖项等级" style="width: 60%"></el-input>
    </el-form-item>
    <el-form-item v-if="textType == '2'" label="参与人类别" prop="persionType">
      <el-select v-model="dataForm.persionType" placeholder="请选择参与人类别" clearable filterable>
        <el-option v-for="item in persionTypeOptions"
                   :label="item.lable"
                   :value="item.value"
                   :key="item.value"
        >
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item v-if="textType == '2'" label="积分" prop="integral">
      <el-input v-model="dataForm.integral" style="width: 200px" type="number" placeholder="积分">
        <template slot="append">积分</template>
      </el-input>
    </el-form-item>
      <el-form-item v-if="textType == '2'" label="团队成员比负责人少" label-width="150px" prop="integralTo">
        <el-input v-model="dataForm.integralTo" style="width: 200px" type="number" placeholder="积分">
          <template slot="append">积分</template>
        </el-input>
      </el-form-item>
      <el-form-item v-if="textType == '1'" label="附件要求" prop="fileAskContent">
        <el-input v-model="dataForm.fileAskContent" style="width: 60%" placeholder="附件要求" type="textarea" autosize></el-input>
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
        textType: '1',
        participateList: [],
        dataForm: {
          integralId: 0,
          participateType: '',
          raceGrade: '',
          prizeGrade: '',
          persionType: null,
          integral: 0,
          integralTo: 0,
          parentId: '',
          fileAskContent: ''
        },
        dataRule: {
          participateType: [
            { required: true, message: '申报类型不能为空', trigger: 'blur' }
          ],
          parentId: [
            { required: true, message: '申报类型不能为空', trigger: 'blur' }
          ],
          raceGrade: [
            { required: true, message: '项目名称不能为空', trigger: 'blur' }
          ],
          integral: [
            { required: true, message: '积分不能为空', trigger: 'blur' }
          ]
        },
        persionTypeOptions: [{lable: '负责人', value: 1}, {lable: '参与人员', value: 2}]
      }
    },
    methods: {
      init (id) {
        this.getTypeList()
        this.dataForm.integralId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.integralId) {
            this.$http({
              url: this.$http.adornUrl(`/points/innovatesyspoints/info/${this.dataForm.integralId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.participateType = data.innovateSysPoints.participateType
                this.dataForm.raceGrade = data.innovateSysPoints.raceGrade
                this.dataForm.prizeGrade = data.innovateSysPoints.prizeGrade
                this.dataForm.persionType = data.innovateSysPoints.persionType
                this.dataForm.integral = data.innovateSysPoints.integral
                this.dataForm.integralTo = data.innovateSysPoints.integralTo
                this.dataForm.fileAskContent = data.innovateSysPoints.fileAskContent
                this.dataForm.parentId = data.innovateSysPoints.parentId
                // eslint-disable-next-line eqeqeq
                if (this.dataForm.parentId == 0) {
                  this.textType = '1'
                } else {
                  this.textType = '2'
                }
              }
            })
          } else {
            this.dataForm.parentId = ''
            this.dataForm.participateType = ''
          }
        })
      },
      getTypeList () {
        this.participateList = []
        this.$http({
          url: this.$http.adornUrl('/points/innovatesyspoints/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 1000,
            'parentId': 0,
            'isDel': 0
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.participateList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.participateList = []
            this.totalPage = 0
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            // 如果父级id和该记录的id相同，则把父级id设置为0
            // eslint-disable-next-line eqeqeq
            if (this.dataForm.parentId == this.dataForm.integralId) {
              this.dataForm.parentId = 0
            }
            // eslint-disable-next-line eqeqeq
            if (this.textType == '1') {
              this.dataForm.raceGrade = ''
              this.dataForm.prizeGrade = ''
              this.dataForm.persionType = ''
              this.dataForm.integral = 0
              this.dataForm.integralTo = 0
              this.dataForm.parentId = 0
            } else {
              this.dataForm.fileAskContent = ''
            }
            this.$http({
              url: this.$http.adornUrl(`/points/innovatesyspoints/${!this.dataForm.integralId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'integralId': this.dataForm.integralId || undefined,
                'participateType': this.dataForm.participateType,
                'raceGrade': this.dataForm.raceGrade,
                'prizeGrade': this.dataForm.prizeGrade,
                'persionType': this.dataForm.persionType,
                'integral': this.dataForm.integral,
                'integralTo': this.dataForm.integralTo,
                'parentId': this.dataForm.parentId,
                'fileAskContent': this.dataForm.fileAskContent,
                'isDel': 0
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
      // 选择申报类型
      changeParticipate (e) {
        this.participateList.forEach(item => {
          // eslint-disable-next-line eqeqeq
          if (item.participateType == e) {
            this.dataForm.parentId = item.integralId
          }
        })
        // eslint-disable-next-line eqeqeq
        if (this.dataForm.parentId == 0) {
          this.dataForm.parentId = ''
        }
      }
    }
  }
</script>
