<template>
  <el-dialog
    title="详情"
    :close-on-click-modal="true"
    :visible.sync="visible"
    :append-to-body="true"
  >
    <table border="1" cellspacing="0" width="100%" class="table">
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr class="contents" align="center">
        <th colspan="10" style="height: 3.2rem">
          实训成果信息
        </th>
      </tr>
      <tr align='center'>
        <th colspan="2">基地名称</th>
        <td colspan="8">{{dataForm.trainingBaseName}}</td>
      </tr>
      <tr align='center'>
      <th colspan="2">二级学院</th>
      <td colspan="8">{{dataForm.instituteName}}</td>
    </tr>
      <tr align='center' >
        <th colspan="2">材料年度</th>
        <td colspan="8">{{dataForm.materialYear}}年</td>
      </tr>
      <tr align='center'>
        <th colspan="2">材料类型</th>
        <td colspan="8">{{dataForm.materialType}}</td>
      </tr>
      <!--附件开始-->
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr align="center" class="contents">
        <th colspan="10" style="height: 3.2rem">附件</th>
      </tr>
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr align='center'>
        <th colspan="7">附件名</th>
        <td colspan="3">操作</td>
      </tr>
      <template>
        <tr v-for="item in attachLists"
            align="center">
          <td colspan="7">{{item.attachName}}</td>
          <td colspan="3"><el-button @click="attachDown(item)" :loading="downloadLoading"><span v-text="downloadText"></span></el-button></td>
        </tr>
      </template>
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <!--附件结束-->
    </table>
  </el-dialog>
</template>

<script>
    export default {
      data () {
        return {
          visible: false,
          url: '',
          downloadLoading: false,
          downloadText: '下载',
          fileAskContent: '无',
          fileIsNull: false,
          fileList: [],
          attachLists: [], // 附件列表
          delAttachLists: [], // 要删除的附件
          dataForm: {
            trainingAchieveId: 0,
            trainingBaseName: '',
            instituteId: '',
            instituteName: '', // 二级学院名称
            materialYear: '',
            materialType: '',
            materialTypeId: '',
            trainingBaseId: '',
            isDel: ''
          },
          textType: '',
          trainingTypes: []
        }
      },
      methods: {
        init (id) {
          this.getInstituteName()
          this.url = this.$http.adornUrl(`/training/innovatetrainingbaseachieve/upload?token=${this.$cookie.get('token')}`)
          this.dataForm.trainingAchieveId = id || 0
          this.visible = true
          this.$http({
            url: this.$http.adornUrl(`/training/innovatetrainingachievetype/list`),
            method: 'get'
          }).then(({data}) => {
            this.trainingTypes = data.page.list
          })
          // eslint-disable-next-line eqeqeq
          this.$nextTick(() => {
            if (this.dataForm.trainingAchieveId) {
              this.$http({
                url: this.$http.adornUrl(`/training/innovatetrainingbaseachieve/info/${this.dataForm.trainingAchieveId}`),
                method: 'get',
                params: this.$http.adornParams()
              }).then(({data}) => {
                if (data && data.code === 0) {
                  this.dataForm.trainingBaseName = data.innovateTrainingBaseAchieveEntity.trainingBaseName
                  this.dataForm.materialYear = data.innovateTrainingBaseAchieveEntity.materialYear
                  this.dataForm.instituteId = data.innovateTrainingBaseAchieveEntity.instituteId
                  this.fomatterInstitute(data.innovateTrainingBaseAchieveEntity)
                  this.dataForm.materialType = data.innovateTrainingBaseAchieveEntity.materialType
                  this.dataForm.materialTypeId = data.innovateTrainingBaseAchieveEntity.materialTypeId
                  this.dataForm.trainingBaseId = data.innovateTrainingBaseAchieveEntity.trainingBaseId
                  this.dataForm.isDel = data.innovateTrainingBaseAchieveEntity.isDel
                  this.attachLists = data.attachEntityList
                }
              })
            }
          })
        },
        // 文件下载
        attachDown (attach) {
          this.downloadLoading = true
          this.downloadText = '正在下载'
          this.$notify({
            title: '下载提示',
            message: '文件正在后台下载,请您稍后!',
            duration: 0,
            type: 'success'
          })
          this.$httpFile({
            url: this.$httpFile.adornUrl(`/training/innovatetrainingbaseattach/download`),
            method: 'post',
            params: this.$httpFile.adornParams({
              'filePath': attach.attachPath
            })
          }).then(response => {
            if (!response) {
              this.$notify({
                title: '下载提示',
                message: '下载失败',
                duration: 0,
                type: 'error'
              })
              this.downloadLoading = false
              return
            }
            let url = window.URL.createObjectURL(new Blob([response.data]))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', attach.attachName)
            document.body.appendChild(link)
            link.click()
            this.$notify({
              title: '下载成功',
              message: '后台文件下载成功',
              duration: 0,
              type: 'success'
            })
            this.downloadText = '下载'
            this.downloadLoading = false
          }).catch(err => {
            console.log(err)
            this.downloadLoading = false
          })
        },
        getInstituteName () {
          debugger
          this.$http({
            url: this.$http.adornUrl(`/innovate/sys/institute/all`),
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.instituteList = data.institute
            }
          })
        },
        // 格式化学院名称
        fomatterInstitute (e) {
          for (let i = 0; i <= this.instituteList.length; i++) {
            if (parseInt(this.instituteList[i].instituteId) === parseInt(e.instituteId)) {
              this.dataForm.instituteName = this.instituteList[i].instituteName
              break
            }
          }
        }
      }
    }
</script>

<style scoped>

</style>
