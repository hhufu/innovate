<template>
  <el-dialog
    title="详情"
    :close-on-click-modal="true"
    :visible.sync="visible"
    :append-to-body="true"
  >
    <table border="1" cellspacing="0" width="100%" class="table" style="table-layout: fixed">
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr class="contents" align="center">
        <th colspan="10" style="height: 3.2rem">
          入住企业信息
        </th>
      </tr>
      <tr align='center'>
        <th colspan="2">企业负责人</th>
        <td colspan="8">{{dataForm.enterpriseDirector}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">企业名称</th>
        <td colspan="8">{{dataForm.enterpriseName}}</td>
      </tr>
      <tr align='center' >
        <th colspan="2">负责人所在学院</th>
        <td colspan="8">{{dataForm.departmentDirector}}</td>
      </tr>
      <tr align='center' >
        <th colspan="2">企业类型</th>
        <td colspan="8">{{dataForm.enterpriseType}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">入驻时间</th>
        <td colspan="8">{{parseTime(dataForm.settledTime, '{y}年{m}月{d}日')}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">主要经营范围</th>
        <td colspan="8">{{dataForm.businessScope}}</td>
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
          downloadLoading: false,
          dataForm: {
            settledEnterpId: 0,
            enterpriseName: '',
            enterpriseUserId: '',
            enterpriseDirector: '',
            departmentDirector: '',
            settledTime: '',
            enterpriseType: '',
            businessScope: '',
            applyStatus: 0,
            isDel: 0
          },
          fileIsNull: false,
          fileList: [],
          url: '',
          downloadText: '下载',
          instituteList: [],
          attachLists: []
        }
      },
      methods: {
        init (id) {
          this.url = this.$http.adornUrl(
            `/enterprise/innovateenterpriseattach/upload?token=${this.$cookie.get(
              'token'
            )}`
          )
          this.dataForm.settledEnterpId = id || 0
          this.visible = true
          this.$nextTick(() => {
            if (this.dataForm.settledEnterpId) {
              this.$http({
                url: this.$http.adornUrl(
                  `/enterprise/innovateenterpriseinfo/info/${
                    this.dataForm.settledEnterpId
                  }`
                ),
                method: 'get',
                params: this.$http.adornParams()
              }).then(({ data }) => {
                if (data && data.code === 0) {
                  this.dataForm.enterpriseName =
                    data.infoModel.infoEntity.enterpriseName
                  this.dataForm.enterpriseUserId =
                    data.infoModel.infoEntity.enterpriseUserId
                  this.dataForm.enterpriseDirector =
                    data.infoModel.infoEntity.enterpriseDirector
                  this.dataForm.departmentDirector =
                    data.infoModel.infoEntity.departmentDirector
                  this.dataForm.settledTime = data.infoModel.infoEntity.settledTime
                  this.dataForm.enterpriseType =
                    data.infoModel.infoEntity.enterpriseType
                  this.dataForm.businessScope =
                    data.infoModel.infoEntity.businessScope
                  this.dataForm.applyStatus = parseInt(
                    data.infoModel.infoEntity.applyStatus || 0
                  )
                  this.dataForm.isDel = data.infoModel.infoEntity.isDel
                  this.attachLists = data.infoModel.attachEntities
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
            url: this.$httpFile.adornUrl(`/enterprise/innovateenterpriseattach/download`),
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
        }
      }
    }
</script>

<style scoped>

</style>
